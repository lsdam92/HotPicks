package com.kitri.hotpicks.member.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.kitri.hotpicks.member.model.MemberDto;
import com.kitri.hotpicks.member.service.MemberService;

@Controller
@RequestMapping("/member")
@SessionAttributes("userInfo")
public class MemberController {

	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private MemberService memberService;
	
	// 가입 페이지 이동
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public void join() {
		System.out.println("c : 가입 페이지 이동 메소드 들어옴");
	}

	// 가입하기
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String join(MemberDto memberDto,
						@RequestParam("profile_file") MultipartFile multipartFile,
						Model model) {
		System.out.println("c : 가입하기 메소드 들어옴");
		
		// 프로필 사진이 있는 경우,
		if(multipartFile != null && !multipartFile.isEmpty()) {
			String orignPicture = multipartFile.getOriginalFilename();
			String realPath = servletContext.getRealPath("/upload/member");
			
			File dir = new File(realPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			
			String savePicture = UUID.randomUUID().toString() + orignPicture.substring(orignPicture.lastIndexOf("."));
			File file = new File(realPath, savePicture);

			System.out.println("저장 경로 = " + realPath);
			System.out.println("db에 저장되는 것 = " + savePicture);
			
			memberDto.setProfile(savePicture); // memberDto에 '사진명.jpg'을 세팅함
			
			try {
				
				multipartFile.transferTo(file); // multipartFile의 파일을 file로 옮김
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // 프로필 사진이 없는 경우,
			memberDto.setProfile("user.png"); // memberDto에 기본 사진 경로를 세팅함
		}
		
		int isJoined = memberService.join(memberDto);
		
		if(isJoined == 1) {
			SimpleDateFormat format = new SimpleDateFormat ( "yyyy년 MM월 dd일");
			String today = format.format(System.currentTimeMillis());
			memberDto.setJoinDate(today);
			model.addAttribute("joinInfo", memberDto);
			return "/member/joinok";			
		} else {
			return "/member/joinfail";
		}
				
	}
	
	// id 중복 체크하기
	@RequestMapping("/idcheck")
	@ResponseBody
	public String idcheck(@RequestParam(name="checkid", defaultValue = "") String userId) {
		String json = memberService.idCheck(userId);
		System.out.println(json);
		return json;
	}

	// 로그인 페이지 이동
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void login() {
		System.out.println("c : 로그인 페이지 이동 메소드 들어옴");
	}

	// 로그인하기
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(MemberDto memberDto, Model model) {
		System.out.println("c : 로그인하기 메소드 들어옴");
				
		MemberDto loginMember = memberService.login(memberDto);
		
		if(loginMember != null) {
			model.addAttribute("userInfo", loginMember);
			return "redirect:/index.jsp";
		} else {
			model.addAttribute("loginfail", "fail");
			return "member/login";
		}
		
	}
	
	// 로그아웃하기
	@RequestMapping(value = "/logout")
	public String logout(@ModelAttribute("userInfo") MemberDto memberDto, SessionStatus sessionStatus) {
		System.out.println("c : 로그아웃하기 메소드 들어옴");
		sessionStatus.setComplete();
		return "redirect:/index.jsp";
	}
	
	// 마이페이지 이동
	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public void mypage(@ModelAttribute("userInfo") MemberDto memberDto, Model model) {
		System.out.println("c : 마이페이지 이동 메소드 들어옴");
		// TODO 마이페이지 이동 이벤트 수정
		// : memberDto의 id값이 가진, 리뷰개수, 리뷰 정보들을 model에 add해주기
	}
	
	// 정보수정 페이지 이동
	@RequestMapping(value="/modify", method = RequestMethod.GET)
	public void modify() {
		System.out.println("c : 정보수정 페이지 이동 메소드 들어옴");
	}
	
	// 정보수정하기
	@RequestMapping(value="/modify", method = RequestMethod.POST)
	public String modify(MemberDto memberDto,
						  @RequestParam("profile_file") MultipartFile multipartFile,
						  @ModelAttribute("userInfo") MemberDto membercurrentDto,
						  Model model) {
		System.out.println("c : 정보수정하기 메소드 들어옴");
		
		// 프로필 사진이 있는 경우,
		if(multipartFile != null && !multipartFile.isEmpty()) {
			String orignPicture = multipartFile.getOriginalFilename();
			String realPath = servletContext.getRealPath("/upload/member");
					
			File dir = new File(realPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
					
			String savePicture = UUID.randomUUID().toString() + orignPicture.substring(orignPicture.lastIndexOf("."));
			File file = new File(realPath, savePicture);

			memberDto.setProfile(savePicture); // memberDto에 '사진명.jpg'을 세팅함
					
			try {
						
				multipartFile.transferTo(file); // multipartFile의 파일을 file로 옮김
						
			} catch (IllegalStateException e) {
						e.printStackTrace();
			} catch (IOException e) {
						e.printStackTrace();
			}
		} else { // 프로필 사진이 없는 경우,
			// 수정을 안하는 경우. 냅둔다.
			memberDto.setProfile(membercurrentDto.getProfile());
		}
		
		memberService.modify(memberDto);
		
		model.addAttribute("userInfo", memberDto);
		
		return "member/mypage";
		
	}
	
	
	// 탈퇴하기
	@RequestMapping("/exit")
	public String exit(@ModelAttribute("userInfo") MemberDto memberDto, SessionStatus sessionStatus) {
		System.out.println("c : 탈퇴하기 메소드 들어옴");
		
		memberService.exit(memberDto);
		
		sessionStatus.setComplete();
		
		return "redirect:/index.jsp";
		
	}
}