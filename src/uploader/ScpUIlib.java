package uploader;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

/**
 *
 * @author zhangqunshi
 */
public class ScpUIlib {

    static final String SEPARATOR = " ==> ";

    public static String format(Map<String, String> fileMapping) {

        Iterator<String> it = fileMapping.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            String value = fileMapping.get(key);
            File tmp = new File(key);
            sb.append(tmp.getName()).append(": ").append(key).append(SEPARATOR).append(value).append("\n");
        }
        return sb.toString();
    }

    public static String backup_format(Map<String, String> fileMapping) {

        Iterator<String> it = fileMapping.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            String value = fileMapping.get(key);
            sb.append(key).append(SEPARATOR).append(value).append("\n");
        }
        return sb.toString();
    }

    public static Map backup_parse(String fileMapping, JRootPane rootPane) {
        Map<String, String> pathMap = new HashMap<String, String>();
        try {
            String[] lines = fileMapping.split("\n");
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].trim().equals("")) {
                    continue;
                }
                if (lines[i].indexOf(SEPARATOR) == -1) {
                    JOptionPane.showMessageDialog(rootPane, "The format of path mapping is not correct, you need use (" + SEPARATOR + ") to combined them!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    return pathMap;
                }
                String[] line = lines[i].split(SEPARATOR);
                File f = new File(line[0].trim());
                if (!f.exists()) {
                    JOptionPane.showMessageDialog(rootPane, "The file " + line[0] + " doesn't exist!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    return pathMap;
                }
                pathMap.put(line[0].trim(), line[1].trim());
            }

        } catch (Exception e) {
            System.err.println("Error read path mapping: " + e.getMessage());
            JOptionPane.showMessageDialog(rootPane, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return pathMap;
    }
}
