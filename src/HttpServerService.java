import java.io.*;
import java.net.Socket;

public class HttpServerService implements Runnable {
    private InputStream ins;
    private OutputStream ous;
    private static final String ROOT = "D:\\imikasa-workspace\\my-http\\www";

    public HttpServerService(Socket client){
        try {
            ins = client.getInputStream();
            ous = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String filePath = dealReq();
            doResponse(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //响应
    private void doResponse(String filePath) {
        File file = new File(ROOT+filePath);
        System.out.println(file.getPath());
        if(file.exists()&&!file.getPath().equals("D:\\imikasa-workspace\\my-http\\www")){
            try{
                String fileType = file.getName().split("\\.")[1];
                if(fileType.equals("html")){
                    String result = RespUtil.doResp("text/html", true, file , ous);
                    System.out.println(result);
                }else if(fileType.equals("jpg")){
                    String result = RespUtil.doResp("image/jpeg", false, file,ous);
                    System.out.println(result);
                }else if(fileType.equals("css")){
                    String result = RespUtil.doResp("text/css", true, file, ous);
                    System.out.println(result);
                }else if(fileType.equals("js")){
                    String result = RespUtil.doResp("application/script", true, file,ous);
                    System.out.println(result);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try {
                StringBuilder err = new StringBuilder();
                String html = "<h1>File not found</h1>";
                err.append("HTTP/1.1 404 Not Found \r\n");
                err.append("Content-Type:text/html \r\n");
                err.append("\r\n");
                err.append(html);
                ous.write(err.toString().getBytes());
                ous.flush();
                ous.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //请求,获取请求的资源路径
    public String dealReq() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        String req = br.readLine();
        if(req!=null){
            String[] reqLine = req.split(" ");
            return reqLine[1];
        }else {
            return "/";
        }
    }

}
