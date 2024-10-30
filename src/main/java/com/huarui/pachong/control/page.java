package com.huarui.pachong.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;
import com.huarui.pachong.pachong.getdatas;
import com.huarui.pachong.pachong.getdatas.dataobject;

@Controller
public class page {
    @GetMapping("/page")
    public String welcome(Model model) {
        getdatas dataFetcher = new getdatas();
        List<dataobject> dataList = dataFetcher.fetchData();
        String mostFrequentCategory = dataFetcher.getMostFrequentCategory();
        List<String> titles = dataList.stream().map(dataobject::getTitle).collect(Collectors.toList());
        List<Integer> viewCounts = dataList.stream().map(dataobject::getViewCount).collect(Collectors.toList());

        dataobject highestViewCountVideo = dataFetcher.getHighestViewCountVideo();
        dataobject highestDanmakuCountVideo = dataFetcher.getHighestDanmakuCountVideo();

        model.addAttribute("highestViewCountVideo", highestViewCountVideo);
        model.addAttribute("highestDanmakuCountVideo", highestDanmakuCountVideo);
        model.addAttribute("dataList", dataList);
        model.addAttribute("mostFrequentCategory", mostFrequentCategory);



        return "page";
    }
}
