package net.hoyoung.demo;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/21.
 */
public class HuiDetailCrawler extends BreadthCrawler {
    JdbcTemplate jdbcTemplate = null;
    private Map<String,String> getLoc(String url){
        Map<String,String> map = new HashMap<>();
        String[] tt = {"province","city","area"};
        String[] sstr = url.substring(url.indexOf("?")+1).split("&");
        int i=0;
        for (; i <sstr.length ; i++) {
            String id = sstr[i].split("=")[1];
            map.put(tt[i],id);
        }
        for (;i<tt.length;i++){
            map.put(tt[i],"0");
        }
        System.out.println(map);
        return map;
    }
    Pattern idRegx = Pattern.compile("id/(\\d+).html");
    @Override
    public void visit(Page page, Links links) {

        Matcher matcher = idRegx.matcher(page.getUrl());
        String id = null;
        if(matcher.find()){
            id = matcher.group(1);
        }
        if(id == null){
            return;
        }
        Html html = new Html(page.getHtml());
        Selectable table = html.xpath("//table");
        String leader = table.xpath("//tr[@class=row2_tr_no]/td/text()").get();
        String email = table.xpath("//tr[2]/td/text()").get();
        String web = table.xpath("//tr[3]/td/a/text()").get();
        String addr = table.xpath("//tr[4]/td/text()").get();
        String tel = table.xpath("//tr[5]/td/text()").get();
        String postcode = table.xpath("//tr[6]/td/text()").get();
        String domain = table.xpath("//tr[7]/td/text()").get();

        String intro = html.xpath("//dd[@class='fr row2_fr']/table//tr[2]/td/html()").get();
        String eintro = html.xpath("//dd[@class='fr row2_fr']/table[2]//tr[2]/td/html()").get();
        String project = html.xpath("//dd[@class='fr row2_fr']/table[3]//tr[2]/td//html()").get();
        jdbcTemplate.update("update org SET leader=?,email=?,web=?,addr=?,tel=?,postcode=?,`domain`=?,intro=?,eintro=?,project=? where id=?",
                leader,
                email,
                web,
                addr,
                tel,
                postcode,
                domain,
                intro,
                eintro,
                project,
                id);
        System.out.println("更新成功");
    }

    public HuiDetailCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        jdbcTemplate = JDBCHelper.createMysqlTemplate("mysql1",
                "jdbc:mysql://localhost/green_org?useUnicode=true&characterEncoding=utf8",
                "root", "", 5, 30);
        List<Integer> ids = jdbcTemplate.queryForList("SELECT id FROM org", Integer.class);
        for (Integer id : ids){
            this.addSeed("http://www.hyi.org.cn/go/Index/detail/id/"+id+".html");
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        HuiDetailCrawler crawler = new HuiDetailCrawler("huiDetailCrawler",false);
        crawler.setThreads(10);
        crawler.setRetry(5);
//        crawler.setTopN(100);
        //crawler.setResumable(true);
        /*start crawl with depth of 4*/
        crawler.start(1);
        System.out.println("耗时："+(System.currentTimeMillis()-start)/1000+"秒");
    }
}
