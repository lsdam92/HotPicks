package com.kitri.hotpicks.contents.dao;

import java.util.List;

import com.kitri.hotpicks.contents.model.ContentsDetailDto;
import com.kitri.hotpicks.contents.model.ContentsDto;
import com.kitri.hotpicks.contents.model.SidoDto;
import com.kitri.hotpicks.contents.model.SigunguDto;

public interface ContentsDao {

//select contents list process
	List<ContentsDto>contentslist();
	//select detail	
	ContentsDetailDto contentsdetail();
	
//contents update process
	//api 목록 list와 db의 list를 비교하여(where contentId in (list)
	//선 delete 진행 후 insert
	
	//void updateContents(int contentsId);

	//delete contents process
	void deleteContents(int contentsId);
	
	void deleteContentsDetail(int contentsId);
	
	//insert contents process
	void insertContents(ContentsDetailDto cdd);
	
	void insertContentsDetail(ContentsDetailDto cdd);
	
	
	
//Location Process
	//insert sido
	void insertSido(List<SidoDto> sidoData);
	//insert sigungu
	void insertSigunguList(List<SigunguDto> sigunguData);

	void insertSigungu(SigunguDto sigunguDto);
	
}