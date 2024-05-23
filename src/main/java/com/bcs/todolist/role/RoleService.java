package com.bcs.todolist.role;

import com.bcs.todolist.common.FileProcessor;
import com.bcs.todolist.role.dto.SaveOrUpdateRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final static String DATA_FILE_NAME = "role.json";
    private FileProcessor fileProcessor;

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Integer id) {
        Optional<Role> role = roleRepository.findById(id);

        if (role.isPresent()) {
            return role.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void saveRole(SaveOrUpdateRoleDto dto) {
        Role role = new Role();
        role.setName(dto.name());

        try {
            roleRepository.save(role);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void updateRole(Integer id, SaveOrUpdateRoleDto dto) {
        if (!roleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        try {
            roleRepository.update(id, dto.name());
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }
}
