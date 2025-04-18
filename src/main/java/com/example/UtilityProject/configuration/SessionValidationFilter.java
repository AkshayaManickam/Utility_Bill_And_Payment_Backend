package com.example.UtilityProject.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SessionValidationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/generate-otp", "/api/auth/verify-otp", "/api/auth/session-status"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String sessionIdFromHeader = request.getHeader("X-Session-Id");
        String sessionIdFromCookie = null;

        // Get session ID from cookies
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (cookie.getName().equals("JSESSIONID")) {
                    sessionIdFromCookie = cookie.getValue();
                }
            }
        }

        // Log session information for debugging
        System.out.println("Request URI: " + path);
        System.out.println("Session ID from header: " + sessionIdFromHeader);
        System.out.println("Session ID from cookie: " + sessionIdFromCookie);

        // Use session ID from header if it exists; otherwise, fall back to cookie
        String sessionIdToUse = (sessionIdFromHeader != null) ? sessionIdFromHeader : sessionIdFromCookie;

        // Skip session validation for public endpoints
        if (PUBLIC_ENDPOINTS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println(sessionIdToUse);

        if (sessionIdToUse != null) {
            HttpSession session = request.getSession(false);

            System.out.println(session);

            if (session != null) {
                // Log the session ID from the request
                System.out.println("Session ID from request: " + session.getId());

                if (sessionIdToUse.equals(session.getId())) {
                    Object contextObj = session.getAttribute("SPRING_SECURITY_CONTEXT");

                    if (contextObj != null) {
                        System.out.println("SecurityContext found in session.");
                        if (contextObj instanceof SecurityContext context) {
                            Authentication authentication = context.getAuthentication();
                            if (authentication != null && authentication.isAuthenticated()) {
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                        } else {
                            // Invalid SecurityContext or expired session
                            System.out.println("SecurityContext is invalid or expired.");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("Invalid or expired session.");
                            return;
                        }
                    } else {
                        // No SecurityContext in the session
                        System.out.println("No SecurityContext found in session.");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid or expired session.");
                        return;
                    }
                } else {
                    // Mismatched session ID
                    System.out.println("Session ID mismatch. Expected: " + sessionIdToUse + ", Found: " + session.getId());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid or expired session.");
                    return;
                }
            } else {
                // No session found
                System.out.println("No session found for the request.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired session.");
                return;
            }
        } else {
            // No session ID provided in the header or cookie
            System.out.println("No session ID found in request.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Session ID is missing.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
