package com.baifendian.solr;

/**
 * Created by yangzheming on 16/4/21.
 */
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SolrUtils {
    private HttpSolrServer server;
    public SolrUtils(String solrServerAddr){
        this.server = new HttpSolrServer(solrServerAddr);
    }

    public static void main(String args[]) throws IOException,SolrServerException{

        SolrUtils solrUtils = new SolrUtils("http://192.168.61.73:20049/solr/Cdouding");
        solrUtils.solrQuery();

        /* solr delete index  */

//        solrUtils.server.deleteById("20");
//        solrUtils.server.deleteByQuery("*:*");
//        solrUtils.server.commit();
    }

    public List<String> solrQuery() throws IOException,SolrServerException{
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
//        query.addFilterQuery("iid:32784");
        query.setFields("title");
        query.setStart(0);
        query.setRows(5000000);
        query.set("defType", "edismax");
        QueryResponse response = server.query(query);
        SolrDocumentList results = response.getResults();
        List<String> list = new ArrayList<String>();
        String result = null;
        int null_cnt = 0;
        for (int i = 0; i < results.size(); ++i) {
            try {
                result = results.get(i).get("title").toString();
            }catch(NullPointerException e){
                e.printStackTrace();
                null_cnt++;
            }
            if(result != null){
                list.add(result);
            }
//            System.out.println(results.get(i).get("title"));
        }
        System.out.println("null_cnt: "+null_cnt);
        return list;
    }

    public void solrInsert() throws IOException,SolrServerException{
        for(int i=0;i<1;++i) {
            Timestamp d = new Timestamp(System.currentTimeMillis());

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("iid", "10001000"+String.valueOf(i));
            doc.addField("cid_iid", "Cjianke"+"_10001000"+String.valueOf(i));
            doc.addField("timestamp", d.getTime()/1000-i );
            doc.addField("name", "替代米沙坦片段(立文)" );
//            System.out.println(d.getTime()/1000-7*24*3600-i);
            doc.addField("bvl",1);
            server.add(doc);
            if(i%100==0) server.commit();  // periodically flush
        }
        server.commit();
    }

}
