package cn.zensezz.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class HttpServletResponse {

    private Integer httpCode = 200;

    private Map<String, List<String>> header;

    private SimplifyOutputStream outputStream = new SimplifyOutputStream();

    public boolean containsHeader(String name) {
        if(header==null){
            return false;
        }
        return header.containsKey(name);
    }
    public Map<String, List<String>> getHeaders() {
        if(header==null){
            return null;
        }
        return header;
    }
    public List<String> getHeader(String name) {
        if(header==null){
            return null;
        }
        return header.get(name);
    }

    public void setHeader(String name, String headerLine) {
        if(header==null){
            header=new ConcurrentHashMap<String, List<String>>();
        }
        if(!header.containsKey(name)){
            List<String> headers=new ArrayList<String>();
            header.put(name, headers);
        }
        header.get(name).clear();
        header.get(name).add(headerLine);
    }
}
