package ru.kpfu.itis.kononenko.gtree2.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kpfu.itis.kononenko.gtree2.dto.request.NodeFormRequest;
import ru.kpfu.itis.kononenko.gtree2.service.NodeService;

@Controller
@RequestMapping("/trees/{treeId}/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    /* -------- страница добавления первого узла -------- */
    @GetMapping("/new")
    public String newForm(@PathVariable Long treeId, Model model) {
        model.addAttribute("nodeForm", NodeFormRequest.builder().build());
        model.addAttribute("treeId", treeId);
        return "add-node";
    }

    /* -------- сохранение первого узла -------- */
    @PostMapping
    public String addInitialNode(@PathVariable Long treeId,
                                 @ModelAttribute("nodeForm") NodeFormRequest form,
                                 BindingResult br) {

        if (br.hasErrors()) return "add-node";

        nodeService.save(treeId, form);
        return "redirect:/trees/" + treeId + "?success=true";
    }



}
