package net.logvv.raven.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.logvv.raven.common.model.GeneralResult;

@RestController
public class PushRest {
	private static final Logger LOGGER = LoggerFactory.getLogger(PushRest.class);
	
	@Autowired
	private PushMessageService pushService;
	
	@RequestMapping(value = "/push/single", method=RequestMethod.POST)
	public GeneralResult spush() 
	{
		GeneralResult result = new GeneralResult();
		LOGGER.info("Begin to push single device.");
		// do push
		try{
			
		}catch(Exception e){
			// 
			LOGGER.info("failed to push notice.")
		}
		
		return result;
		
	}

}
