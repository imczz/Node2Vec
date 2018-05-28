package czzUI;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 图文件过滤器
 * @author CZZ*/
public class GraphFileFilter extends FileFilter{

	/**
	 * 过滤函数*/
	@Override
	public boolean accept(File file) {
		boolean ret = true;
		if(file.isDirectory())  
            ret = true;  
        else  
        {  
            String fileName = file.getName();  
            int index = fileName.lastIndexOf('.');
    		if (index > 0 && index < fileName.length() - 1) {
    			String extension = fileName.substring(index + 1).toLowerCase();			//文件拓展名（不分大小写）
    			if (extension.equals("edgelist")) ret = true;
    		}
            else ret = false;  
        }
		return ret;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "邻接表文件(*.edgelist)";
	}

}
