package ru.project.calculations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.calculations.dto.user.UsersPayloadNew;
import ru.project.calculations.dto.user.UsersPayloadUpdate;
import ru.project.calculations.enums.UserStatus;
import ru.project.calculations.service.RolesService;
import ru.project.calculations.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final RolesService rolesService;

	@GetMapping
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public String findAllUsers(Model model,
							   Principal userDetails) {
		model.addAttribute("users", userService.findAllUsers());
		model.addAttribute("userDetails", userDetails);
		return "users/user-menu";
	}

	@GetMapping("/create")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public String createUserForm(@ModelAttribute("user") UsersPayloadNew payload,
								 Principal userDetails,
								 Model model) {
		model.addAttribute("users", userService.findAllUsers());
		model.addAttribute("userStatus", UserStatus.values());
		model.addAttribute("roles", rolesService.findAllRoles());
		model.addAttribute("userDetails", userDetails);
		return "users/user-create";
	}

	@PostMapping("/create")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public String createUser(@Validated @ModelAttribute("user") UsersPayloadNew payload,
							 BindingResult bindingResult,
							 Principal userDetails,
							 Model model,
							 RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("users", userService.findAllUsers());
			model.addAttribute("userStatus", UserStatus.values());
			model.addAttribute("roles", rolesService.findAllRoles());
			model.addAttribute("userDetails", userDetails);
			return "users/user-create";
		} else {
			var user = userService.createUser(payload);
			model.addAttribute("userDetails", userDetails);
			attributes.addFlashAttribute("successMessage", "success.object.update");
			return "redirect:/users/view-update/%d".formatted(user.getId());
		}

	}

	@GetMapping("/view-update/{id:\\d+}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public String findUsersByUserIdForm(@PathVariable long id,
										Principal userDetails,
										Model model) {
		model.addAttribute("users", userService.findAllUsers());
		model.addAttribute("user", userService.findUsersByUserId(id));
		model.addAttribute("userStatus", UserStatus.values());
		model.addAttribute("roles", rolesService.findAllRoles());
		model.addAttribute("userDetails", userDetails);
		return "users/user-view-update";
	}

	@PostMapping("/view-update")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public String findUsersByUserId(@Validated @ModelAttribute("user") UsersPayloadUpdate payload,
									BindingResult bindingResult,
									Principal userDetails,
									Model model,
									RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("users", userService.findAllUsers());
			model.addAttribute("userStatus", UserStatus.values());
			model.addAttribute("roles", rolesService.findAllRoles());
			model.addAttribute("userDetails", userDetails);
			return "users/user-view-update";
		} else {
			var user = userService.updateUser(payload);
			rolesService.setUserRoles(payload);
			attributes.addFlashAttribute("successMessage", "success.object.update");
			return "redirect:/users/view-update/%d".formatted(user.getId());
		}
	}

	@GetMapping("/delete/{id:\\d+}")
	public String deleteUser(@PathVariable long id,
							 RedirectAttributes attributes) {
		userService.deleteUserById(id);
		attributes.addFlashAttribute("successMessage", "success.object.deleted");
		return "redirect:/users";
	}

}