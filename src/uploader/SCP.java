/*
 * zhangqunshi@126.com
 * 2013-7-16
 */
package uploader;

import ch.ethz.ssh2.*;
import java.io.File;
import java.io.IOException;

public class SCP {

    private String usr;
    private String pwd;
    private String host;

    public SCP(String host, String user, String password) {
        this.usr = user;
        this.pwd = password;
        this.host = host;
    }

    public void upload(String localFile, String remotePath) throws Exception {
        Connection con = new Connection(host);
        try {
            
            con.connect();
            boolean isAuthed = con.authenticateWithPassword(usr, pwd);
            System.out.println("isAuthed: " + isAuthed);

            
            File srcFilePath = new File(localFile);
            if (srcFilePath.isDirectory()) {
                putDir(con, localFile, remotePath, "0644");
            } else {
                System.out.println("upload: " + localFile + " to " + remotePath);
                SCPClient scpClient = con.createSCPClient();
                scpClient.put(localFile, remotePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail to upload: " + e.getMessage());
            throw e;
        } finally {
            if (con != null)  {
                con.close();
            }
        }
    }
    
    public void putDir(Connection conn, String localDir, String remoteDir, String mode) throws IOException {
        File srcLocalDir = new File(localDir);

        String new_dir = remoteDir + "/" + srcLocalDir.getName();
        createDir(conn, new_dir);

        File[] fs = srcLocalDir.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                putDir(conn, f.getAbsolutePath(), new_dir, mode);
            }
            else {
                SCPClient cli =  conn.createSCPClient();
                System.out.println("upload: " + f.getAbsolutePath() + " to " + new_dir);
                cli.put(f.getAbsolutePath(), new_dir, mode);
            }

        }


    }
    
    
    public void createDir(Connection conn, String remoteDir) throws IOException {
        Session s = conn.openSession();
        try {
            System.out.println("create dir: " + remoteDir);
            s.execCommand("mkdir -p -m 755 " + remoteDir);
            s.waitForCondition(ChannelCondition.EOF, 0);
        } finally {
            s.close();
        }
        
    }

}
