package com.bcs.todolist.role;

import com.bcs.todolist.role.dto.GetRoleDto;
import com.bcs.todolist.role.dto.SaveOrUpdateRoleDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/role")
public class RoleController {
    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping
    public List<GetRoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public GetRoleDto getRoleById(@PathVariable("id") Integer id) {
        return roleService.getRoleById(id);
    }

    @PostMapping
    public void saveRole(@Valid @RequestBody SaveOrUpdateRoleDto role) {
        roleService.saveRole(role);
    }

    @PutMapping("/{id}")
    public void updateRole(@PathVariable("id") Integer id, @Valid @RequestBody SaveOrUpdateRoleDto role) {
        roleService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
    }
}
