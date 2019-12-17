package com.profcristianoaf81.jsffileuploaddemo;

import java.io.File;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author cristianoaf81
 */

@ManagedBean(name="fileUploadBean")
@RequestScoped
public class FileUploadBean {
    // variaveis
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; //2mb
    FacesMessage message = new FacesMessage();
    private Part file;//usado para obter o arquivo e seus dados

    // getters and setters
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }    
    
    
    // realiza o upload
    public void upload()
    {   
        // strings usadas para identificar o so
        String WINDOWS = "c://uploaded";
        String LINUX = System.getProperty("user.home")+"/uploaded";
        
        // determina de acordo com so o path do arquivo
        String filePath = System.getProperty("os.name").startsWith("windows")
                ? WINDOWS : LINUX;
        // obtem apenas o nome do arquivo sem extensao
        String fileName = extractFileName(file);
        
        try{
            // verifica se arquivo obedece aos limites (<= 2mb e jpg/png)            
            if(file.getSize() <= MAX_FILE_SIZE 
                    && "image/jpeg".equals(file.getContentType()) 
                    || "image/png".equals(file.getContentType()))
            {
                // cria o arq vazio
                File saveFileInDir = new File(filePath);
                // se nao existir o loca cria
                if(!saveFileInDir.exists())
                        saveFileInDir.mkdir();
                // escreve o arquivo no local indicado
                file.write(filePath+File.separator+fileName);
                // mostra mensagem ao usuario
                String filePathAndName = filePath+File.separator+fileName;
                message.setSummary("Upload realizado com sucesso em: "
                        +filePathAndName);
                message.setSeverity(FacesMessage.SEVERITY_INFO);
            }else{
                message.setSummary("não é um arquivo de imagem válido");
                message.setSeverity(FacesMessage.SEVERITY_WARN);
            }
            
            if(!"".equals(message.getSummary()))
                FacesContext.getCurrentInstance().addMessage(null, message);
            
            
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
       
    // codigo auxiliar pode ser inserido em um util
    public String extractFileName(Part part)
    {
        String contentDisposition = part.getHeader("content-disposition");
        String[] items =   contentDisposition.split(";");
        for(String content : items)
        {
            System.out.println(content);//mostra o cont. do cabecalho
            
            if(content.trim().startsWith("filename"))
            {
                return content.substring(content.indexOf("=")+1)
                        .trim().replace("\"", "");
            }
            
        }
        
        return "";
    }

}
