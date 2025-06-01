package ru.kpfu.itis.kononenko.gtree2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.NodeForm;
import ru.kpfu.itis.kononenko.gtree2.entity.Node;
import ru.kpfu.itis.kononenko.gtree2.service.FamilyService;

@Controller
@RequestMapping("/trees/{treeId}/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final FamilyService familyService;

    /* -------- страница добавления первого узла -------- */
    @GetMapping("/new")
    public String showAddNodeForm(@PathVariable Long treeId, Model model) {
        model.addAttribute("nodeForm", new NodeForm());
        model.addAttribute("treeId", treeId);
        return "add-node";
    }

    /* -------- сохранение первого узла -------- */
    @PostMapping
    public String addInitialNode(@PathVariable Long treeId,
                                 @ModelAttribute("nodeForm") NodeForm form,
                                 BindingResult br) {

        if (br.hasErrors()) return "add-node";

//        Node node = new Node(
//                null,
//                treeId,
//                form.getFirstName(),
//                form.getLastName(),
//                form.getGender(),
//                form.getBirthDate(),
//                form.getDeathDate(),
//                form.getComment(),
//                null,
//                null);

        familyService.addInitialNode(node);
        return "redirect:/trees/" + treeId + "?success=true";
    }
}
