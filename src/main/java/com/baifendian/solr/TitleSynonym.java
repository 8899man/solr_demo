package com.baifendian.solr;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by yangzheming on 16/5/21.
 */
public class TitleSynonym {
    public static void main(String[] args) throws SolrServerException, IOException{
        SolrUtils solrUtils = new SolrUtils("http://192.168.61.73:20049/solr/Cdouding");
        List<String> list = solrUtils.solrQuery();
        JiebaSegmenter segmenter = new JiebaSegmenter();
        Set<String> set = new HashSet<>();
        Iterator<String> title_it = list.iterator();
        while(title_it.hasNext()){
            List<SegToken> resultList = segmenter.process(title_it.next(), SegMode.INDEX);
            for (SegToken token : resultList){
                if(token.word.length() > 1 && token.word.length() < 20 && !isInt(token.word)){
                    set.add(token.word);
                }
            }
        }
        Iterator<String> set_it = set.iterator();
        try{
            File file = new File("a.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            int cnt = 0;
            while(set_it.hasNext()){
                bw.write(set_it.next().toString());
                bw.write("\n");
                cnt++;
                if(cnt % 1000 == 0){
                    bw.flush();
                }
            }
            bw.flush();
            bw.close();
            fw.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static boolean isInt(String s){
        try{
            if(Integer.valueOf(s).getClass().toString().equals("class java.lang.Integer")){
                return true;
            }
        }catch (Exception e){
        }
        return false;
    }
}