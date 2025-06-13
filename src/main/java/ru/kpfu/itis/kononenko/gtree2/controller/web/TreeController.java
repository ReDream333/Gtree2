package ru.kpfu.itis.kononenko.gtree2.controller.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeFormRequest;
import ru.kpfu.itis.kononenko.gtree2.dto.request.TreeCreateRequest;
import ru.kpfu.itis.kononenko.gtree2.entity.Tree;
import ru.kpfu.itis.kononenko.gtree2.service.TreeService;
import ru.kpfu.itis.kononenko.gtree2.service.security.CustomUserDetails;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/trees")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService treeService;


    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("treeForm", TreeFormRequest.builder().build());
        return "tree-form";
    }


    @PostMapping
    public String createTree(
            @AuthenticationPrincipal
            CustomUserDetails userDetails,
            @ModelAttribute("treeForm")
            TreeFormRequest form,
            BindingResult br)
    {
        if (br.hasErrors()) {
            return "tree-form";
        }
        Long userId = userDetails.getUser().getId();
        long treeId = treeService.createTree(
               TreeCreateRequest
                       .builder()
                       .userId(userId)
                       .name(form.treeName())
                       .isPrivate(form.privateTree())
                       .build()
        );
        return "redirect:/trees/" + treeId + "/nodes/new";
    }


    @GetMapping("/my")
    public String listUserTrees(
            @AuthenticationPrincipal
            CustomUserDetails userDetails,
            Model model
    ){
        Long userId = userDetails.getUser().getId();
        List<Tree> userTrees = treeService.getTreesByUserId(userId);
        model.addAttribute("userTrees", userTrees);
        return "user-trees";
    }


    @GetMapping("/public")
    public String listPublicTrees(Model model) {
        List<Tree> publicTrees = treeService.getPublicTrees();
        model.addAttribute("publicTrees", publicTrees);
        return "public-trees";
    }


    @GetMapping("/{treeId}")
    public String viewTree(
            @PathVariable Long treeId,
            Model model)
    {

        treeService.getTree(treeId);

        String nodesJson = treeService.nodesAsJson(treeId);
        String linksJson = treeService.relationsAsJson(treeId);

        model.addAttribute("nodes", nodesJson);
        model.addAttribute("links", linksJson);

        return "view";
    }
}
