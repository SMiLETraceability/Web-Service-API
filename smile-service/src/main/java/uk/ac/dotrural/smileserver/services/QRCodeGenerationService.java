/**
 * 
 */
package uk.ac.dotrural.smileserver.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

/**
 * @author Charles Ofoegbu
 *
 */

@Path("/qrcode")
public class QRCodeGenerationService {
    
    private static Logger logger = LoggerFactory.getLogger(QRCodeGenerationService.class);
    
    @GET
    @Produces({ MediaType.APPLICATION_OCTET_STREAM})
    public void getQRCode(@Context HttpServletRequest req, @Context HttpServletResponse res) {
	logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> GET called - get QR code " );	
	String data = req.getParameter("data");
		
	if(req.getParameter("fileName") != null){
	    String fileName = req.getParameter("fileName");
	    res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName +new Timestamp(new Date().getTime())+".png" + "\"");
	}
	
	ByteArrayOutputStream out = QRCode.from(data).to(ImageType.PNG).stream();
	res.setContentType("image/png");
	res.setContentLength(out.size());
	OutputStream outStream;
	try {
	    outStream = res.getOutputStream();
	    outStream.write(out.toByteArray());
	    outStream.flush();
	    outStream.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }
}
