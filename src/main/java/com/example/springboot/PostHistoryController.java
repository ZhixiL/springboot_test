package com.example.springboot;

import com.example.utils.PostLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostHistoryController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/api")
    public String postString(@RequestParam("post_input_text") String inputTest,
                             Model model) {
        model.addAttribute("title","Post Page");
        System.out.println(inputTest);
        String pathToLoggFile = env.getProperty("post.log.file");
        System.out.println(pathToLoggFile);
        PostLogger pl = new PostLogger(pathToLoggFile);
        pl.writeLine(inputTest);
        return "index";
    }


    @RequestMapping(value = "/history")
    public String getHistoryString(Model model) {
        String pathToLoggFile = env.getProperty("post.log.file");
        PostLogger pl = new PostLogger(pathToLoggFile);
        String history = pl.readHistory();
        model.addAttribute("history",history);
        model.addAttribute("newLineChar", '\n');
        return "history";
    }

    @RequestMapping(value = "/delete")
    public String deletePost(@RequestParam("post_text") String deleteText, 
    				Model model) {
	   	model.addAttribute("title", "Delete Page");
	   	String pathToLogFile = env.getProperty("post.log.file");
	   	PostLogger pl = new PostLogger(pathToLogFile);
		if (!deleteText.isEmpty())
		{
			boolean deleted = pl.deleteString(deleteText);
			model.addAttribute("deleted", deleted);
			model.addAttribute("deleteAttempted", true);
		}
        return "delete";
    }

}

