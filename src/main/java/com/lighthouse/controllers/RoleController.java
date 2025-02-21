package com.lighthouse.controllers;

import com.lighthouse.models.Role;
import com.lighthouse.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("create-role")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @GetMapping("get-all-roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }


}
