package com.nercl.music.cloud.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.nercl.music.cloud.entity.impress.AccompanyImpress;
import com.nercl.music.cloud.entity.impress.BeatImpress;
import com.nercl.music.cloud.entity.impress.BkgImpress;
import com.nercl.music.cloud.entity.impress.ComposerImpress;
import com.nercl.music.cloud.entity.impress.FuDianImpress;
import com.nercl.music.cloud.entity.impress.Impress;
import com.nercl.music.cloud.entity.impress.IntervalImpress;
import com.nercl.music.cloud.entity.impress.MelodyLineImpress;
import com.nercl.music.cloud.entity.impress.ModeImpress;
import com.nercl.music.cloud.entity.impress.ModelSingingImpress;
import com.nercl.music.cloud.entity.impress.MusicMarksImpress;
import com.nercl.music.cloud.entity.impress.MusicalFormDescImpress;
import com.nercl.music.cloud.entity.impress.MusicalFormImpress;
import com.nercl.music.cloud.entity.impress.NationalityAreaImpress;
import com.nercl.music.cloud.entity.impress.PitchImpress;
import com.nercl.music.cloud.entity.impress.RhythmImpress;
import com.nercl.music.cloud.entity.impress.RuoQiImpress;
import com.nercl.music.cloud.entity.impress.SpeedImpress;
import com.nercl.music.cloud.entity.impress.TypesLiteratureImpress;
import com.nercl.music.cloud.entity.impress.VolumeRangeImpress;
import com.nercl.music.cloud.entity.resource.Dimension;
import com.nercl.music.cloud.entity.song.Song;
import com.nercl.music.cloud.service.ImpressService;
import com.nercl.music.cloud.service.ResourceService;
import com.nercl.music.cloud.service.SongService;
import com.nercl.music.constant.CList;
import com.nercl.music.exception.LogicException;

@Component
public class SongParser {

	@Autowired
	private SongService songService;

	@Autowired
	private ImpressService impressService;

	@Autowired
	private ResourceService resourceService;

	@Value("${dd-yinyue.zip}")
	private String zipFilePath;

	@Autowired
	private Gson gson;

	@SuppressWarnings("unchecked")
	public void parse(String path) throws IOException {
		File json = new File(path + File.separator + "json.json");
		if (null == json || !json.exists()) {
			throw new LogicException(CList.Api.Client.PROCESSING_FAILED, "no json file found");
		}
		List<String> lines = FileUtils.readLines(json, Charset.forName("UTF-8"));
		if (null == lines || lines.isEmpty()) {
			throw new LogicException(CList.Api.Client.PROCESSING_FAILED, "no json file found");
		}
		Map<String, Object> map = gson.fromJson(lines.get(0), Map.class);
		String name = (String) map.getOrDefault("res_name", "");
		if (Strings.isNullOrEmpty(name)) {
			throw new LogicException(CList.Api.Client.PROCESSING_FAILED, "no file name found");
		}
		String resFile = (String) map.getOrDefault("res_file", "");
		if (Strings.isNullOrEmpty(resFile)) {
			throw new LogicException(CList.Api.Client.PROCESSING_FAILED, "no file found");
		}
		File res = new File(path + File.separator + resFile);
		if (null == res || !res.exists()) {
			throw new LogicException(CList.Api.Client.PROCESSING_FAILED, "no file found");
		}
		Song song = songService.save(name);
		if (null == song) {
			throw new LogicException(CList.Api.Client.PROCESSING_FAILED, "add song failed");
		}
		String ext = Files.getFileExtension(resFile).toLowerCase();
		Map<String, String> params = Maps.newHashMap();
		params.put("fileName", resFile);
		params.put("resourceType", "SONG_RESOURCE");
		String rid = resourceService.save(gson.toJson(params), new FileInputStream(res), resFile, ext);

		songService.addResourceSongRelation(song.getId(), rid, true);

		Map<String, String> nationalityAreaMap = (Map<String, String>) map.getOrDefault("nationality_area", null);
		if (null != nationalityAreaMap) {
			String nationalityName = nationalityAreaMap.get("name");
			String summary = nationalityAreaMap.get("summary");
			File nationalityFile = new File(path + File.separator + nationalityName);
			if (!Strings.isNullOrEmpty(nationalityName) && null != nationalityFile && nationalityFile.exists()) {
				ext = Files.getFileExtension(nationalityName).toLowerCase();
				params = Maps.newHashMap();
				params.put("fileName", nationalityName);
				params.put("resourceType", "SONG_RESOURCE");
				rid = resourceService.save(gson.toJson(params), new FileInputStream(nationalityFile), nationalityName,
						ext);
				NationalityAreaImpress nationalityAreaImpress = new NationalityAreaImpress();
				nationalityAreaImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				nationalityAreaImpress.setResourceId(rid);
				nationalityAreaImpress.setSongId(song.getId());
				nationalityAreaImpress.setSummary(summary);
				nationalityAreaImpress.setDimension(Dimension.NATIONALITY_AREA);
				impressService.add(nationalityAreaImpress);
			}
		}

		Map<String, String> composerMap = (Map<String, String>) map.getOrDefault("composer", null);
		if (null != composerMap) {
			String composerName = composerMap.get("name");
			String summary = composerMap.get("summary");
			File composerFile = new File(path + File.separator + composerName);
			if (!Strings.isNullOrEmpty(composerName) && null != composerFile && composerFile.exists()) {
				ext = Files.getFileExtension(composerName).toLowerCase();
				params = Maps.newHashMap();
				params.put("fileName", composerName);
				params.put("resourceType", "SONG_RESOURCE");
				rid = resourceService.save(gson.toJson(params), new FileInputStream(composerFile), composerName, ext);
				ComposerImpress composerImpress = new ComposerImpress();
				composerImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				composerImpress.setResourceId(rid);
				composerImpress.setSongId(song.getId());
				composerImpress.setSummary(summary);
				composerImpress.setDimension(Dimension.COMPOSER);
				impressService.add(composerImpress);
			}
		}

		Map<String, String> bkgMap = (Map<String, String>) map.getOrDefault("bkg", null);
		if (null != bkgMap) {
			String bkgName = bkgMap.get("name");
			String summary = bkgMap.get("summary");
			File bkgFile = new File(path + File.separator + bkgName);
			if (!Strings.isNullOrEmpty(bkgName) && null != bkgFile && bkgFile.exists()) {
				ext = Files.getFileExtension(bkgName).toLowerCase();
				params = Maps.newHashMap();
				params.put("fileName", bkgName);
				params.put("resourceType", "SONG_RESOURCE");
				rid = resourceService.save(gson.toJson(params), new FileInputStream(bkgFile), bkgName, ext);
				BkgImpress bkgImpress = new BkgImpress();
				bkgImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				bkgImpress.setResourceId(rid);
				bkgImpress.setSongId(song.getId());
				bkgImpress.setSummary(summary);
				bkgImpress.setDimension(Dimension.BKG);
				impressService.add(bkgImpress);
			}
		}

//		Map<String, String> vigorPlayMap = (Map<String, String>) map.getOrDefault("vigor_play", null);
//		if (null != vigorPlayMap) {
//			String vigorPlayName = vigorPlayMap.get("name");
//			String summary = vigorPlayMap.get("summary");
//			File vigorPlayFile = new File(path + File.separator + vigorPlayName);
//			if (!Strings.isNullOrEmpty(vigorPlayName) && null != vigorPlayFile && vigorPlayFile.exists()) {
//				ext = Files.getFileExtension(vigorPlayName).toLowerCase();
//				params = Maps.newHashMap();
//				params.put("fileName", vigorPlayName);
//				params.put("resourceType", "SONG_RESOURCE");
//				rid = resourceService.save(gson.toJson(params), new FileInputStream(vigorPlayFile), vigorPlayName, ext);
//				VigorPlayImpress vigorPlayImpress = new VigorPlayImpress();
//				vigorPlayImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
//				vigorPlayImpress.setResourceId(rid);
//				vigorPlayImpress.setSongId(song.getId());
//				vigorPlayImpress.setSummary(summary);
//				vigorPlayImpress.setDimension(Dimension.VIGOR_PLAY);
//				impressService.add(vigorPlayImpress);
//			}
//		}
//
//		Map<String, String> graceNoteMap = (Map<String, String>) map.getOrDefault("grace_note", null);
//		if (null != graceNoteMap) {
//			String graceNoteName = graceNoteMap.get("name");
//			String summary = graceNoteMap.get("summary");
//			File vigorPlayFile = new File(path + File.separator + graceNoteName);
//			if (!Strings.isNullOrEmpty(graceNoteName) && null != vigorPlayFile && vigorPlayFile.exists()) {
//				ext = Files.getFileExtension(graceNoteName).toLowerCase();
//				params = Maps.newHashMap();
//				params.put("fileName", graceNoteName);
//				params.put("resourceType", "SONG_RESOURCE");
//				rid = resourceService.save(gson.toJson(params), new FileInputStream(vigorPlayFile), graceNoteName, ext);
//				GraceNoteImpress graceNoteImpress = new GraceNoteImpress();
//				graceNoteImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
//				graceNoteImpress.setResourceId(rid);
//				graceNoteImpress.setSongId(song.getId());
//				graceNoteImpress.setSummary(summary);
//				graceNoteImpress.setDimension(Dimension.GRACE_NOTE);
//				impressService.add(graceNoteImpress);
//			}
//		}
		
		Map<String, String> musicMarksMap = (Map<String, String>) map.getOrDefault("music_marks", null);
		if (null != musicMarksMap) {
			String musicMarksName = musicMarksMap.get("name");
			String summary = musicMarksMap.get("summary");
			File vigorPlayFile = new File(path + File.separator + musicMarksName);
			if (!Strings.isNullOrEmpty(musicMarksName) && null != vigorPlayFile && vigorPlayFile.exists()) {
				ext = Files.getFileExtension(musicMarksName).toLowerCase();
				params = Maps.newHashMap();
				params.put("fileName", musicMarksName);
				params.put("resourceType", "SONG_RESOURCE");
				rid = resourceService.save(gson.toJson(params), new FileInputStream(vigorPlayFile), musicMarksName, ext);
				MusicMarksImpress musicMarksImpress = new MusicMarksImpress();
				musicMarksImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				musicMarksImpress.setResourceId(rid);
				musicMarksImpress.setSongId(song.getId());
				musicMarksImpress.setSummary(summary);
				musicMarksImpress.setDimension(Dimension.MUSIC_MARKS);
				impressService.add(musicMarksImpress);
			}
		}

		Map<String, String> typesLiteratureMap = (Map<String, String>) map.getOrDefault("types_literature", null);
		if (null != typesLiteratureMap) {
			String typesLiteratureName = typesLiteratureMap.get("name");
			String summary = typesLiteratureMap.get("summary");
			File typesLiteratureFile = new File(path + File.separator + typesLiteratureName);
			if (!Strings.isNullOrEmpty(typesLiteratureName) && null != typesLiteratureFile
					&& typesLiteratureFile.exists()) {
				ext = Files.getFileExtension(typesLiteratureName).toLowerCase();
				params = Maps.newHashMap();
				params.put("fileName", typesLiteratureName);
				params.put("resourceType", "SONG_RESOURCE");
				rid = resourceService.save(gson.toJson(params), new FileInputStream(typesLiteratureFile),
						typesLiteratureName, ext);
				TypesLiteratureImpress typesLiteratureImpress = new TypesLiteratureImpress();
				typesLiteratureImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				typesLiteratureImpress.setResourceId(rid);
				typesLiteratureImpress.setSongId(song.getId());
				typesLiteratureImpress.setSummary(summary);
				typesLiteratureImpress.setDimension(Dimension.TYPES_LITERATURE);
				impressService.add(typesLiteratureImpress);
			}
		}

		List<Map<String, String>> modes = (List<Map<String, String>>) map.getOrDefault("mode", null);
		if (null != modes && !modes.isEmpty()) {
			modes.forEach(mode -> {
				String keySig = mode.getOrDefault("keysig", "");
				String type = mode.getOrDefault("type", "");
				String contentKeyNote = mode.getOrDefault("content_keynote", "");
				String contentMode = mode.getOrDefault("content_mode", "");
				ModeImpress modeImpress = new ModeImpress();
				modeImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				modeImpress.setKeySig(keySig);
				modeImpress.setType(type);
				modeImpress.setContentKeyNote(contentKeyNote);
				modeImpress.setContentMode(contentMode);
				modeImpress.setSongId(song.getId());
				modeImpress.setDimension(Dimension.MODE);
				impressService.add(modeImpress);
			});
		}

		List<Map<String, Object>> musicalForms = (List<Map<String, Object>>) map.getOrDefault("musical_form", null);
		if (null != musicalForms && !musicalForms.isEmpty()) {
			musicalForms.forEach(musicalForm -> {
				String text = (String) musicalForm.getOrDefault("text", "");
				Number startBar = (Number) musicalForm.getOrDefault("start_bar", null);
				Number endBar = (Number) musicalForm.getOrDefault("end_bar", null);
				MusicalFormImpress musicalFormImpress = new MusicalFormImpress();
				musicalFormImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				musicalFormImpress.setText(text);
				musicalFormImpress.setStartBar(null == startBar ? null : startBar.intValue());
				musicalFormImpress.setEndBar(null == endBar ? null : endBar.intValue());
				musicalFormImpress.setSongId(song.getId());
				musicalFormImpress.setDimension(Dimension.MUSICAL_FORM);
				impressService.add(musicalFormImpress);
			});
		}

		String musicalFormsDesc = (String) map.getOrDefault("musical_form_desc", null);
		MusicalFormDescImpress musicalFormDescImpress = new MusicalFormDescImpress();
		musicalFormDescImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		musicalFormDescImpress.setMusicalFormDesc(musicalFormsDesc);
		musicalFormDescImpress.setSongId(song.getId());
		musicalFormDescImpress.setDimension(Dimension.MUSICAL_FORM_DESC);
		impressService.add(musicalFormDescImpress);

		Boolean hasMelodyLine = (Boolean) map.getOrDefault("melody_line", false);
		MelodyLineImpress melodyLineImpress = new MelodyLineImpress();
		melodyLineImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		melodyLineImpress.setHasMelodyLine(hasMelodyLine);
		melodyLineImpress.setSongId(song.getId());
		melodyLineImpress.setDimension(Dimension.MELODY_LINE);
		impressService.add(melodyLineImpress);

		Number speed = (Number) map.getOrDefault("speed", null);

		SpeedImpress speedImpress = new SpeedImpress();
		speedImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		speedImpress.setSpeed(null == speed ? null : speed.intValue());
		speedImpress.setSongId(song.getId());
		speedImpress.setDimension(Dimension.SPEED);
		impressService.add(speedImpress);

		String beat = (String) map.getOrDefault("beat", "");
		BeatImpress beatImpress = new BeatImpress();
		beatImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		beatImpress.setBeat(beat);
		beatImpress.setSongId(song.getId());
		beatImpress.setDimension(Dimension.BEAT);
		impressService.add(beatImpress);

		String pitch = (String) map.getOrDefault("pitch", "");
		PitchImpress pitchImpress = new PitchImpress();
		pitchImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		pitchImpress.setPitch(pitch);
		pitchImpress.setSongId(song.getId());
		pitchImpress.setDimension(Dimension.PITCH);
		impressService.add(pitchImpress);

		String volumeRange = (String) map.getOrDefault("volume_range", "");
		VolumeRangeImpress volumeRangeImpress = new VolumeRangeImpress();
		volumeRangeImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		volumeRangeImpress.setVolumeRange(volumeRange);
		volumeRangeImpress.setSongId(song.getId());
		volumeRangeImpress.setDimension(Dimension.VOLUME_RANGE);
		impressService.add(volumeRangeImpress);

		List<Map<String, String>> intervals = (List<Map<String, String>>) map.getOrDefault("interval", null);
		if (null != intervals && !intervals.isEmpty()) {
			intervals.forEach(interval -> {
				String inter = interval.getOrDefault("interval", "");
				String notea = interval.getOrDefault("notea", "");
				String noteb = interval.getOrDefault("noteb", "");
				IntervalImpress intervalImpress = new IntervalImpress();
				intervalImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				intervalImpress.setIntervals(inter);
				intervalImpress.setNotea(notea);
				intervalImpress.setNoteb(noteb);
				intervalImpress.setSongId(song.getId());
				intervalImpress.setDimension(Dimension.INTERVAL);
				impressService.add(intervalImpress);
			});
		}

		String rhythm = (String) map.getOrDefault("rhythm", "");
		RhythmImpress rhythmImpress = new RhythmImpress();
		rhythmImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		rhythmImpress.setRhythm(rhythm);
		rhythmImpress.setSongId(song.getId());
		rhythmImpress.setDimension(Dimension.RHYTHM);
		impressService.add(rhythmImpress);

		Boolean hasRuoQi = (Boolean) map.getOrDefault("ruo_qi", false);
		RuoQiImpress ruoQiImpress = new RuoQiImpress();
		ruoQiImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		ruoQiImpress.setHasRuoQi(hasRuoQi);
		ruoQiImpress.setSongId(song.getId());
		ruoQiImpress.setDimension(Dimension.RUO_QI);
		impressService.add(ruoQiImpress);

		Boolean hasFuDian = (Boolean) map.getOrDefault("fu_dian", false);
		FuDianImpress fuDianImpress = new FuDianImpress();
		fuDianImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
		fuDianImpress.setHasFuDian(hasFuDian);
		fuDianImpress.setSongId(song.getId());
		fuDianImpress.setDimension(Dimension.FU_DIAN);
		impressService.add(fuDianImpress);

//		List<Map<String, String>> attachments = (List<Map<String, String>>) map.getOrDefault("attachments", null);
//		if (null == attachments || attachments.isEmpty()) {
//			return;
//		}
//		attachments.forEach(attachment -> {
//			String summary = (String) attachment.getOrDefault("summary", "");
//			String aname = (String) attachment.getOrDefault("name", "");
//			File attachmentFile = new File(path + File.separator + aname);
//			if (Strings.isNullOrEmpty(aname) || null == attachmentFile || !attachmentFile.exists()) {
//				return;
//			}
//			String aext = Files.getFileExtension(aname).toLowerCase();
//			Map<String, String> aparams = Maps.newHashMap();
//			aparams.put("fileName", aname);
//			aparams.put("resourceType", "SONG_RESOURCE");
//			aparams.put("description", summary);
//			String arid = null;
//			try {
//				arid = resourceService.save(gson.toJson(aparams), new FileInputStream(attachmentFile), aname, aext);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			songService.addResourceSongRelation(song.getId(), arid, false);
//		});

		Map<String, String> accompanyMap = (Map<String, String>) map.getOrDefault("accompany", null);
		if (null != accompanyMap) {
			String accompanyName = accompanyMap.get("name");
			String summary = accompanyMap.get("summary");
			File accompanyFile = new File(path + File.separator + accompanyName);
			if (!Strings.isNullOrEmpty(accompanyName) && null != accompanyFile && accompanyFile.exists()) {
				ext = Files.getFileExtension(accompanyName).toLowerCase();
				params = Maps.newHashMap();
				params.put("fileName", accompanyName);
				params.put("resourceType", "SONG_RESOURCE");
				rid = resourceService.save(gson.toJson(params), new FileInputStream(accompanyFile), accompanyName, ext);
				AccompanyImpress accompanyImpress = new AccompanyImpress();
				accompanyImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				accompanyImpress.setResourceId(rid);
				accompanyImpress.setSongId(song.getId());
				accompanyImpress.setSummary(summary);
				accompanyImpress.setDimension(Dimension.ACCOMPANY);
				impressService.add(accompanyImpress);
			}
		}

		Map<String, String> modeSingingMap = (Map<String, String>) map.getOrDefault("model_singing", null);
		if (null != modeSingingMap) {
			String modeSingingName = modeSingingMap.get("name");
			String summary = modeSingingMap.get("summary");
			File modeSingingNameFile = new File(path + File.separator + modeSingingName);
			if (!Strings.isNullOrEmpty(modeSingingName) && null != modeSingingNameFile
					&& modeSingingNameFile.exists()) {
				ext = Files.getFileExtension(modeSingingName).toLowerCase();
				params = Maps.newHashMap();
				params.put("fileName", modeSingingName);
				params.put("resourceType", "SONG_RESOURCE");
				rid = resourceService.save(gson.toJson(params), new FileInputStream(modeSingingNameFile),
						modeSingingName, ext);
				ModelSingingImpress modelSingingImpress = new ModelSingingImpress();
				modelSingingImpress.setField(Impress.Field.MIDDLE_PRIMARY_SCHOOL);
				modelSingingImpress.setResourceId(rid);
				modelSingingImpress.setSongId(song.getId());
				modelSingingImpress.setSummary(summary);
				modelSingingImpress.setDimension(Dimension.MODEL_SINGING);
				impressService.add(modelSingingImpress);
			}
		}
	}
}
