package xin.cosmos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    // 初始化已开放的茶品信息
    static List<Map<String, String>> aUrlMaps = new LinkedList() {{
        add(new HashMap() {{
            put("tag", "票据承兑信用信息披露查询");
            put("href", "billAcceptance");
        }});
        add(new HashMap() {{
            put("tag", "关于我");
            put("href", "about");
        }});
    }};
    @GetMapping("/")
    public String homePage(ModelMap map) {
        map.addAttribute("name", "欢迎使用Spyder - 数据服务");
        map.addAttribute("urls", aUrlMaps);
        return "index";
    }

    /**
     * 票据承兑信用信息披露查询
     * @param map
     * @return
     */
    @GetMapping("/billAcceptance")
    public String billAcceptancePage(ModelMap map) {
        map.addAttribute("name", "票据承兑信用信息披露查询");
        return "billAcceptance";
    }

    @GetMapping("/about")
    public String aboutPage(ModelMap map) {
        map.addAttribute("name", "关于我");
        return "about";
    }

}
