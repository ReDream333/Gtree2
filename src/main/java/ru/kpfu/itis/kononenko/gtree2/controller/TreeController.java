package ru.kpfu.itis.kononenko.gtree2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.TreeForm;
import ru.kpfu.itis.kononenko.gtree2.service.impl.TreeService;

import java.sql.Timestamp;

@Controller
@RequestMapping("/trees")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService treeService;

    /* -------- страница создания дерева -------- */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("treeForm", new TreeForm());
        return "tree-form";
    }


    //TODO без сессий
    @PostMapping
    public String createTree(
            @ModelAttribute("treeForm") TreeForm form,
            BindingResult br) {

        if (br.hasErrors()) return "tree-form";

        long treeId = treeService.createTree(
                currentUser.id(),
                form.getTreeName(),
                form.isPrivate(),
                new Timestamp(System.currentTimeMillis()));

        session.setAttribute("currentTreeId", treeId);
        return "redirect:/trees/" + treeId + "/nodes/new";
    }

    /* -------- просмотр дерева -------- */
    @GetMapping("/{treeId}")
    public String viewTree(@PathVariable Long treeId, Model model) {
        model.addAttribute("nodes", treeService.convertNodesToJson(treeId));
        model.addAttribute("links", treeService.convertRelationsToJson(treeId));
        return "view";
    }

    @GetMapping("/public")
    public String publicTrees(Model model) {
        model.addAttribute("publicTrees", treeService.getPublicTrees());
        return "public-trees";
    }
}
