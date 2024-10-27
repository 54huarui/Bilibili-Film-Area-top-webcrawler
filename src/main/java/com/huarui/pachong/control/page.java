package com.huarui.pachong.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import com.huarui.pachong.pachong.getdatas;
import com.huarui.pachong.pachong.getdatas.dataobject;

import java.util.List;

@Controller
public class page {
    @GetMapping("/page")
    public String welcome(Model model) {
        getdatas dataFetcher = new getdatas();
        List<dataobject> dataList = dataFetcher.fetchData();


        dataobject highestViewCountVideo = dataFetcher.getHighestViewCountVideo();
        dataobject highestDanmakuCountVideo = dataFetcher.getHighestDanmakuCountVideo();

        model.addAttribute("highestViewCountVideo", highestViewCountVideo);
        model.addAttribute("highestDanmakuCountVideo", highestDanmakuCountVideo);

        model.addAttribute("dataList", dataList); // 将数据添加到模型中
        return "page"; // 返回模板名称
    }
}