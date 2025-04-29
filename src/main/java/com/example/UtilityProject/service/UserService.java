package com.example.UtilityProject.service;

import com.example.UtilityProject.model.User;
import com.example.UtilityProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogService auditLogService;

    public List<User> getUniqueUsers() {
        // Fetch only users who are not deleted
        return userRepository.findByIsDeletedFalse();
    }

    public User updateUser(Long id, User userDetails, String employeeEmail) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            String oldValue = String.format(
                    "Name: %s, Email: %s, Phone: %s, Address: %s, Units: %s",
                    existingUser.getName(),
                    existingUser.getEmail(),
                    existingUser.getPhone(),
                    existingUser.getAddress(),
                    existingUser.getUnitsConsumption()
            );
            existingUser.setCustomerId(userDetails.getCustomerId());
            existingUser.setServiceConnectionNo(userDetails.getServiceConnectionNo());
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPhone(userDetails.getPhone());
            existingUser.setAddress(userDetails.getAddress());
            existingUser.setUnitsConsumption(userDetails.getUnitsConsumption());
            existingUser.setStartDate(userDetails.getStartDate());

            User savedUser = userRepository.save(existingUser);
            String newValue = String.format(
                    "Name: %s, Email: %s, Phone: %s, Address: %s, Units: %s",
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getPhone(),
                    savedUser.getAddress(),
                    savedUser.getUnitsConsumption()
            );

            auditLogService.logWithValues(
                    employeeEmail,
                    "UPDATE_USER",
                    "USER_ID: " + savedUser.getCustomerId(),
                    oldValue,
                    newValue,
                    "Updated user profile."
            );

            return savedUser;
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    public void deleteUser(Long id, String employeeEmail) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            StringBuilder oldValues = new StringBuilder();
            oldValues.append("CustomerId: ").append(user.getCustomerId()).append(", ");
            oldValues.append("ServiceConnectionNo: ").append(user.getServiceConnectionNo()).append(", ");
            oldValues.append("Name: ").append(user.getName()).append(", ");
            oldValues.append("Email: ").append(user.getEmail()).append(", ");
            oldValues.append("Phone: ").append(user.getPhone()).append(", ");
            oldValues.append("Address: ").append(user.getAddress()).append(", ");
            oldValues.append("StartDate: ").append(user.getStartDate()).append(", ");
            oldValues.append("UnitsConsumption: ").append(user.getUnitsConsumption());
            user.setDeleted(true);
            userRepository.save(user);
            auditLogService.log(
                    employeeEmail,
                    "DELETE_USER",
                    "USER_ID: " + user.getCustomerId(),
                    "Deleted Values => [" + oldValues + "]"
            );
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }


    public Long getUserCount() {
        List<User> activeUsers = userRepository.findByIsDeletedFalse();
        return (long) activeUsers.size();
    }

    public int getUnitsConsumedByServiceConnectionNo(String serviceConnectionNo) {
        Optional<User> userOptional = userRepository.findByServiceConnectionNo(serviceConnectionNo);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getUnitsConsumption();
        } else {
            throw new RuntimeException("User with service connection number " + serviceConnectionNo + " not found.");
        }
    }


}

