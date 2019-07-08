package com.kitri.hotpicks.contents.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kitri.hotpicks.common.service.CommonService;
import com.kitri.hotpicks.contents.model.ReviewDto;
import com.kitri.hotpicks.contents.service.ReviewService;
import com.kitri.hotpicks.member.model.MemberDto;

@Controller
@RequestMapping("/review")
public class ReviewController {
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ReviewService reviewService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String list(int contentsId) {
//		System.out.println("리스트뽑으러 컨트롤러 도착");
		String json = reviewService.reviewlist(contentsId);
		//System.out.println("json : " + json);
		return json;
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(ReviewDto reviewDto, 
						@RequestParam Map<String, String> parameter, 
						Model model, HttpSession session,
						@RequestParam("picture") MultipartFile multipartFile) {
		//System.out.println("ReviewController 들어왔다!!");
		String path = "";
		
		MemberDto memberDto = (MemberDto) session.getAttribute("userInfo");
		if(memberDto != null) {
			int rseq = commonService.getReNextSeq();
			reviewDto.setSeq(rseq);
			reviewDto.setUserId(memberDto.getUserId());
			
			//contents아이디
			reviewDto.setContentsId(630609);
			
			
			if(multipartFile != null && !multipartFile.isEmpty()) {
				String orignPicture = multipartFile.getOriginalFilename();
				
				String realPath = servletContext.getRealPath("/upload/review");
				DateFormat df = new SimpleDateFormat("yyMMdd");
				String saveFolder = df.format(new Date());
				String realSaveFolder = realPath + File.separator + saveFolder;
				File dir = new File(realSaveFolder);
				if(!dir.exists()) {
					dir.mkdirs();
				}
				String savePicture = UUID.randomUUID().toString() + orignPicture.substring(orignPicture.lastIndexOf('.'));
				
				File file = new File(realSaveFolder, savePicture);
				
				try {
					multipartFile.transferTo(file);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				reviewDto.setOrignPicture(orignPicture);
				reviewDto.setSavePicture(savePicture);
				reviewDto.setSaveFolder(saveFolder); 
			}
			rseq = reviewService.writeArticle(reviewDto);
			
			if(rseq != 0) {
				model.addAttribute("rseq", rseq);
				path = "contents/writeok";
			} else {
				path = "contents/writefail";
			}
		} else {
			path = "contents/writefail";
		}
		model.addAttribute("parameter", parameter);
		return path;
	}

}
