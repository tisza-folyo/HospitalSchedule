package com.hospital.backend.security.person;

import com.hospital.backend.exception.ResourceNotFoundException;
import com.hospital.backend.model.Person;
import com.hospital.backend.model.Role;
import com.hospital.backend.repository.PersonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.hospital.backend.repository.RoleRepository;


@Service
@RequiredArgsConstructor
public class HospitalPersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Person not found with email: " + email));
        return HospitalPersonDetails.buildUserDetails(person);
    }

    @NonNull
    public UserDetails loadUserByEmailAndRole(String email, String roleName) {
        String cleanRoleName = roleName.replace("ROLE_", "");
        Role role = roleRepository.findByRoleName(cleanRoleName).orElseThrow(() -> new ResourceNotFoundException("Role"));
        Person person = personRepository.findByEmailAndRole(email, role)
                .orElseThrow(() -> new ResourceNotFoundException("Person"));

        return HospitalPersonDetails.buildUserDetails(person);
    }
}
