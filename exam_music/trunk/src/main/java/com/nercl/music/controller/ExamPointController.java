package com.nercl.music.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nercl.music.entity.ExamPoint;
import com.nercl.music.entity.ExamRoom;
import com.nercl.music.entity.RequiredPrivilege;
import com.nercl.music.entity.user.Role;
import com.nercl.music.service.ExamPointService;
import com.nercl.music.service.ExamRoomService;

@Controller
public class ExamPointController {

	@Autowired
	private ExamPointService examPointService;

	@Autowired
	private ExamRoomService examRoomService;

	@GetMapping(value = "/exam_points/json")
	@ResponseBody
	public List<Map<String, Object>> json() {
		List<Map<String, Object>> ret = Lists.newArrayList();
		List<ExamPoint> examPoints = this.examPointService.list();
		if (null == examPoints || examPoints.isEmpty()) {
			return ret;
		}
		examPoints.forEach(examPoint -> {
			Map<String, Object> pointMap = Maps.newHashMap();
			ret.add(pointMap);
			pointMap.put("id", examPoint.getId());
			pointMap.put("name", examPoint.getName());
			List<ExamRoom> rooms = this.examRoomService.getByExamPoint(examPoint.getId());
			if (null != rooms && !rooms.isEmpty()) {
				List<Map<String, String>> roomsList = Lists.newArrayList();
				pointMap.put("rooms", roomsList);
				rooms.forEach(room -> {
			        Map<String, String> map = Maps.newHashMap();
			        roomsList.add(map);
			        map.put("id", room.getId());
			        map.put("name", room.getTitle());
		        });
			}
		});
		return ret;
	}

	/**
	 * 查询所有考点
	 * 
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/exam_points")
	public String list(@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<ExamPoint> examPoints = this.examPointService.list(page);
		Map<String, List<ExamRoom>> rooms = Maps.newHashMap();
		if (null != examPoints && !examPoints.isEmpty()) {
			examPoints.forEach(examPoint -> {
				List<ExamRoom> rs = this.examRoomService.getByExamPoint(examPoint.getId());
				rooms.put(examPoint.getId(), rs);
			});
		}
		model.addAttribute("examPoints", examPoints);
		model.addAttribute("rooms", rooms);
		return "examPoint/examPoints";
	}

	/**
	 * 多条件查询考场
	 * 
	 * @param name
	 * @param room
	 * @param point
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/exam_points", params = { "name", "address" })
	@RequiredPrivilege(Role.MANAGER)
	public String query(@RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "address", required = false) String address,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<ExamPoint> examPoints = this.examPointService.listByAttributes(page, name, address);
		model.addAttribute("examPoints", examPoints);
		return "examPoint/examPoints";
	}

	/**
	 * 添加考场页面
	 * 
	 * @return
	 */
	@GetMapping(value = "/exam_point")
	public String toAdd() {
		return "examPoint/examPoint";
	}

	/**
	 * 添加考场
	 * 
	 * @return
	 */
	@PostMapping(value = "/exam_point")
	public String add(String name, String address) {
		if (Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(address)) {
			return "redirect:/exam_point";
		}
		boolean add = examPointService.save(name, address);
		if (add) {
			return "redirect:/exam_points";
		}
		return "redirect:/exam_point";
	}

	/**
	 * 添加考室页面
	 * 
	 */
	@GetMapping(value = "/exam_rooms/add/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String toAddExamRoom(@PathVariable String id, Model model) {
		ExamPoint examPoint = examPointService.get(id);
		if (examPoint != null) {
			model.addAttribute("examPoint", examPoint);
		}
		return "examPoint/examRoom";
	}

	/**
	 * 考室添加
	 * 
	 */
	@PostMapping(value = "/exam_rooms/add/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String addExamRoom(@PathVariable String id, String title, Model model) {
		if (Strings.isNullOrEmpty(title)) {
			return "redirect:/exam_rooms/add/" + id;
		}
		examRoomService.addRoom(id, title);
		return "redirect:/exam_rooms/" + id;
	}

	/**
	 * 考室修改页面
	 * 
	 */
	@GetMapping(value = "/exam_room/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String toEditRoom(@PathVariable String id, String title, Model model) {
		ExamRoom examRoom = examRoomService.getRoom(id);
		model.addAttribute("examRoom", examRoom);
		ExamPoint examPoint = examPointService.get(examRoom.getExamPointId());
		model.addAttribute("examPoint", examRoom.getExamPointId());
		if (examPoint != null) {
			model.addAttribute("examPoint", examPoint);
		}
		return "examPoint/examRoom";
	}

	/**
	 * 考室修改
	 * 
	 */
	@PostMapping(value = "/exam_room/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String editRoom(@PathVariable String id, String title, Model model) {
		String examPointId = examRoomService.update(id, title);
		model.addAttribute("examRoomId", id);
		return "redirect:/exam_rooms/" + examPointId;
	}

	/**
	 * 查询考室by考点
	 * 
	 * @param id
	 * @param page
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/exam_rooms/{id}")
	public String listRooms(@PathVariable String id,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		ExamPoint examPoint = examPointService.get(id);
		if (examPoint != null) {
			List<ExamRoom> examRooms = this.examRoomService.getByExamPoint(id);
			model.addAttribute("examRooms", examRooms);
			model.addAttribute("examPoint", examPoint);
		}
		return "examPoint/examRooms";
	}

	/**
	 * 考点修改页面
	 * 
	 */
	@GetMapping(value = "/exam_point/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String toEdit(@PathVariable String id, Model model) {
		ExamPoint examPoint = examPointService.get(id);
		model.addAttribute("examPoint", examPoint);
		return "examPoint/examPoint";
	}

	/**
	 * 考场修改
	 * 
	 */
	@PostMapping(value = "/exam_point/{id}")
	@RequiredPrivilege(Role.MANAGER)
	public String edit(@PathVariable String id, String name, String address, Model model) {
		examPointService.update(id, name, address);
		return "redirect:/exam_points";
	}

	/**
	 * 删除考点
	 *
	 */
	@GetMapping("/exam_point/delete/{id}")
	@ResponseBody
	public boolean delete(@PathVariable String id) {
		examPointService.delete(id);
		return true;
	}

}
