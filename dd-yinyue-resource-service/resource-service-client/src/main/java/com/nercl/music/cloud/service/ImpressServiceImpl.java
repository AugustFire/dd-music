package com.nercl.music.cloud.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.nercl.music.cloud.dao.ImpressDao;
import com.nercl.music.cloud.entity.impress.AccompanyImpress;
import com.nercl.music.cloud.entity.impress.BeatImpress;
import com.nercl.music.cloud.entity.impress.BkgImpress;
import com.nercl.music.cloud.entity.impress.ComposerImpress;
import com.nercl.music.cloud.entity.impress.FuDianImpress;
import com.nercl.music.cloud.entity.impress.GraceNoteImpress;
import com.nercl.music.cloud.entity.impress.Impress;
import com.nercl.music.cloud.entity.impress.Impress.Field;
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
import com.nercl.music.cloud.entity.impress.VigorPlayImpress;
import com.nercl.music.cloud.entity.impress.VolumeRangeImpress;
import com.nercl.music.cloud.entity.resource.Dimension;
import com.nercl.music.cloud.entity.resource.Resource;

@Service
@Transactional
public class ImpressServiceImpl implements ImpressService {

	@Autowired
	private ImpressDao impressDao;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private Gson gson;

	@Override
	public List<Map<String, Object>> getImpress(String summary, Dimension dimension) {
		List<Impress> impresses = impressDao.get(summary, dimension);
		if (null == impresses || impresses.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> res = Lists.newArrayList();
		impresses.forEach(impress -> {
			res.add(toJson(impress));
		});
		return res;
	}

	@Override
	public Impress get(String id) {
		return impressDao.findByID(id);
	}

	private Map<String, Object> toJson(Impress impress) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", impress.getId());
		if (impress instanceof NationalityAreaImpress) {
			map.put("dimension", Dimension.NATIONALITY_AREA);
			NationalityAreaImpress nationalityAreaImpress = (NationalityAreaImpress) impress;
			Resource resource = nationalityAreaImpress.getResource();
			if (null != resource) {
				map.put("rid", resource.getId());
				map.put("name", resource.getName());
			}
			map.put("summary", nationalityAreaImpress.getSummary());
		}
		if (impress instanceof ComposerImpress) {
			map.put("dimension", Dimension.COMPOSER);
			ComposerImpress composerImpress = (ComposerImpress) impress;
			Resource resource = composerImpress.getResource();
			if (null != resource) {
				map.put("rid", resource.getId());
				map.put("name", resource.getName());
			}
			map.put("summary", composerImpress.getSummary());
		}
		if (impress instanceof BkgImpress) {
			map.put("dimension", Dimension.BKG);
			BkgImpress bkgImpress = (BkgImpress) impress;
			Resource resource = bkgImpress.getResource();
			if (null != resource) {
				map.put("rid", resource.getId());
				map.put("name", resource.getName());
			}
			map.put("summary", bkgImpress.getSummary());
		}
		// if (impress instanceof GraceNoteImpress) {
		// map.put("dimension", Dimension.GRACE_NOTE);
		// GraceNoteImpress graceNoteImpress = (GraceNoteImpress) impress;
		// Resource resource = graceNoteImpress.getResource();
		// if (null != resource) {
		// map.put("rid", resource.getId());
		// map.put("name", resource.getName());
		// }
		// map.put("summary", graceNoteImpress.getSummary());
		// }
		// if (impress instanceof VigorPlayImpress) {
		// map.put("dimension", Dimension.VIGOR_PLAY);
		// VigorPlayImpress vigorPlayImpress = (VigorPlayImpress) impress;
		// Resource resource = vigorPlayImpress.getResource();
		// if (null != resource) {
		// map.put("rid", resource.getId());
		// map.put("name", resource.getName());
		// }
		// map.put("summary", vigorPlayImpress.getSummary());
		// }
		if (impress instanceof VigorPlayImpress) {
			map.put("dimension", Dimension.MUSIC_MARKS);
			MusicMarksImpress musicMarksImpress = (MusicMarksImpress) impress;
			Resource resource = musicMarksImpress.getResource();
			if (null != resource) {
				map.put("rid", resource.getId());
				map.put("name", resource.getName());
			}
			map.put("summary", musicMarksImpress.getSummary());
		}
		if (impress instanceof TypesLiteratureImpress) {
			map.put("dimension", Dimension.TYPES_LITERATURE);
			TypesLiteratureImpress typesLiteratureImpress = (TypesLiteratureImpress) impress;
			Resource resource = typesLiteratureImpress.getResource();
			if (null != resource) {
				map.put("rid", resource.getId());
				map.put("name", resource.getName());
			}
			map.put("summary", typesLiteratureImpress.getSummary());
		}
		if (impress instanceof BeatImpress) {
			map.put("dimension", Dimension.BEAT);
			BeatImpress beatImpress = (BeatImpress) impress;
			String beat = beatImpress.getBeat();
			map.put("beat", beat);
		}
		if (impress instanceof FuDianImpress) {
			map.put("dimension", Dimension.FU_DIAN);
			FuDianImpress fuDianImpress = (FuDianImpress) impress;
			boolean hasFuDian = fuDianImpress.getHasFuDian();
			map.put("has_fu_dian", hasFuDian);
		}
		if (impress instanceof IntervalImpress) {
			map.put("dimension", Dimension.INTERVAL);
			IntervalImpress intervalImpress = (IntervalImpress) impress;
			String interval = intervalImpress.getIntervals();
			String notea = intervalImpress.getNotea();
			String noteb = intervalImpress.getNoteb();
			map.put("interval", interval);
			map.put("notea", notea);
			map.put("noteb", noteb);
		}
		if (impress instanceof MelodyLineImpress) {
			map.put("dimension", Dimension.MELODY_LINE);
			MelodyLineImpress melodyLineImpress = (MelodyLineImpress) impress;
			boolean hasMelodyLine = melodyLineImpress.getHasMelodyLine();
			map.put("has_melody_line", hasMelodyLine);
		}
		if (impress instanceof ModeImpress) {
			map.put("dimension", Dimension.MODE);
			ModeImpress modeImpress = (ModeImpress) impress;
			String keySig = modeImpress.getKeySig();
			String type = modeImpress.getType();
			String contentKeyNote = modeImpress.getContentKeyNote();
			String contentMode = modeImpress.getContentMode();
			map.put("key_sig", keySig);
			map.put("type", type);
			map.put("content_key_note", contentKeyNote);
			map.put("content_mode", contentMode);
		}
		if (impress instanceof MusicalFormImpress) {
			map.put("dimension", Dimension.MUSICAL_FORM);
			MusicalFormImpress musicalFormImpress = (MusicalFormImpress) impress;
			String text = musicalFormImpress.getText();
			Integer startBar = musicalFormImpress.getStartBar();
			Integer endBar = musicalFormImpress.getEndBar();
			map.put("text", text);
			map.put("start_bar", startBar);
			map.put("end_bar", endBar);
		}
		if (impress instanceof MusicalFormDescImpress) {
			map.put("dimension", Dimension.MUSICAL_FORM_DESC);
			MusicalFormDescImpress musicalFormDescImpress = (MusicalFormDescImpress) impress;
			String desc = musicalFormDescImpress.getMusicalFormDesc();
			map.put("musical_form_desc", desc);
		}
		if (impress instanceof PitchImpress) {
			map.put("dimension", Dimension.PITCH);
			PitchImpress pitchImpress = (PitchImpress) impress;
			String pitch = pitchImpress.getPitch();
			map.put("pitch", pitch);
		}
		if (impress instanceof RhythmImpress) {
			map.put("dimension", Dimension.RHYTHM);
			RhythmImpress thythmImpress = (RhythmImpress) impress;
			String rhythm = thythmImpress.getRhythm();
			map.put("rhythm", rhythm);
		}
		if (impress instanceof RuoQiImpress) {
			map.put("dimension", Dimension.RUO_QI);
			RuoQiImpress ruoQiImpress = (RuoQiImpress) impress;
			boolean hasRuoQi = ruoQiImpress.getHasRuoQi();
			map.put("has_ruo_qi", hasRuoQi);
		}
		if (impress instanceof SpeedImpress) {
			map.put("dimension", Dimension.SPEED);
			SpeedImpress speedImpress = (SpeedImpress) impress;
			Integer speed = speedImpress.getSpeed();
			map.put("speed", speed);
		}
		if (impress instanceof VolumeRangeImpress) {
			map.put("dimension", Dimension.VOLUME_RANGE);
			VolumeRangeImpress volumeRangeImpress = (VolumeRangeImpress) impress;
			String volumeRange = volumeRangeImpress.getVolumeRange();
			map.put("volume_range", volumeRange);
		}
		if (impress instanceof AccompanyImpress) {
			map.put("dimension", Dimension.ACCOMPANY);
			AccompanyImpress accompanyImpress = (AccompanyImpress) impress;
			Resource resource = accompanyImpress.getResource();
			if (null != resource) {
				map.put("rid", resource.getId());
				map.put("name", resource.getName());
			}
			map.put("summary", accompanyImpress.getSummary());
		}
		if (impress instanceof ModelSingingImpress) {
			map.put("dimension", Dimension.MODEL_SINGING);
			ModelSingingImpress modelSingingImpress = (ModelSingingImpress) impress;
			Resource resource = modelSingingImpress.getResource();
			if (null != resource) {
				map.put("rid", resource.getId());
				map.put("name", resource.getName());
			}
			map.put("summary", modelSingingImpress.getSummary());
		}
		return map;
	}

	@Override
	public boolean add(Impress impress) {
		impressDao.save(impress);
		return null != impress && !Strings.isNullOrEmpty(impress.getId());
	}

	@Override
	public boolean add(String sid, Map<String, Object> map, InputStream in, String filename, String ext)
			throws IOException {
		String dimen = (String) map.getOrDefault("dimension", "");
		Dimension dimension = null;
		try {
			dimension = Dimension.valueOf(dimen);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (Dimension.NATIONALITY_AREA.equals(dimension)) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return false;
			}
			NationalityAreaImpress nationalityAreaImpress = new NationalityAreaImpress();
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				nationalityAreaImpress.setResourceId(rid);
				nationalityAreaImpress.setSummary(summary);
				nationalityAreaImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
				nationalityAreaImpress.setDimension(Dimension.NATIONALITY_AREA);
				nationalityAreaImpress.setSongId(sid);
				impressDao.save(nationalityAreaImpress);
				return !Strings.isNullOrEmpty(nationalityAreaImpress.getId());
			}
		}
		if (Dimension.COMPOSER.equals(dimension)) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return false;
			}
			ComposerImpress composerImpress = new ComposerImpress();
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				composerImpress.setResourceId(rid);
				composerImpress.setSummary(summary);
				composerImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
				composerImpress.setDimension(Dimension.COMPOSER);
				composerImpress.setSongId(sid);
				impressDao.save(composerImpress);
				return !Strings.isNullOrEmpty(composerImpress.getId());
			}
		}
		if (Dimension.BKG.equals(dimension)) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return false;
			}
			BkgImpress bkgImpress = new BkgImpress();
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				bkgImpress.setResourceId(rid);
				bkgImpress.setSummary(summary);
				bkgImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
				bkgImpress.setDimension(Dimension.BKG);
				bkgImpress.setSongId(sid);
				impressDao.save(bkgImpress);
				return !Strings.isNullOrEmpty(bkgImpress.getId());
			}
		}
		// if (Dimension.GRACE_NOTE.equals(dimension)) {
		// if (null == in || Strings.isNullOrEmpty(filename) ||
		// Strings.isNullOrEmpty(ext)) {
		// return false;
		// }
		// GraceNoteImpress graceNoteImpress = new GraceNoteImpress();
		// String summary = (String) map.getOrDefault("summary", "");
		// Map<String, String> params = Maps.newHashMap();
		// params.put("fileName", filename);
		// params.put("resourceType", "SONG_RESOURCE");
		// String rid = resourceService.save(gson.toJson(params), in, filename,
		// ext);
		// if (!Strings.isNullOrEmpty(rid)) {
		// graceNoteImpress.setResourceId(rid);
		// graceNoteImpress.setSummary(summary);
		// graceNoteImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
		// graceNoteImpress.setDimension(Dimension.GRACE_NOTE);
		// graceNoteImpress.setSongId(sid);
		// impressDao.save(graceNoteImpress);
		// return !Strings.isNullOrEmpty(graceNoteImpress.getId());
		// }
		// }
		// if (Dimension.VIGOR_PLAY.equals(dimension)) {
		// if (null == in || Strings.isNullOrEmpty(filename) ||
		// Strings.isNullOrEmpty(ext)) {
		// return false;
		// }
		// VigorPlayImpress vigorPlayImpress = new VigorPlayImpress();
		// String summary = (String) map.getOrDefault("summary", "");
		// Map<String, String> params = Maps.newHashMap();
		// params.put("fileName", filename);
		// params.put("resourceType", "SONG_RESOURCE");
		// String rid = resourceService.save(gson.toJson(params), in, filename,
		// ext);
		// if (!Strings.isNullOrEmpty(rid)) {
		// vigorPlayImpress.setResourceId(rid);
		// vigorPlayImpress.setSummary(summary);
		// vigorPlayImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
		// vigorPlayImpress.setDimension(Dimension.VIGOR_PLAY);
		// vigorPlayImpress.setSongId(sid);
		// impressDao.save(vigorPlayImpress);
		// return !Strings.isNullOrEmpty(vigorPlayImpress.getId());
		// }
		// }
		if (Dimension.MUSIC_MARKS.equals(dimension)) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return false;
			}
			MusicMarksImpress musicMarksImpress = new MusicMarksImpress();
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				musicMarksImpress.setResourceId(rid);
				musicMarksImpress.setSummary(summary);
				musicMarksImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
				musicMarksImpress.setDimension(Dimension.MUSIC_MARKS);
				musicMarksImpress.setSongId(sid);
				impressDao.save(musicMarksImpress);
				return !Strings.isNullOrEmpty(musicMarksImpress.getId());
			}
		}
		if (Dimension.TYPES_LITERATURE.equals(dimension)) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return false;
			}
			TypesLiteratureImpress typesLiteratureImpress = new TypesLiteratureImpress();
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				typesLiteratureImpress.setResourceId(rid);
				typesLiteratureImpress.setSummary(summary);
				typesLiteratureImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
				typesLiteratureImpress.setDimension(Dimension.TYPES_LITERATURE);
				typesLiteratureImpress.setSongId(sid);
				impressDao.save(typesLiteratureImpress);
				return !Strings.isNullOrEmpty(typesLiteratureImpress.getId());
			}
		}
		if (Dimension.BEAT.equals(dimension)) {
			BeatImpress beatImpress = new BeatImpress();
			String beat = (String) map.getOrDefault("beat", "");
			beatImpress.setBeat(beat);
			beatImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			beatImpress.setDimension(Dimension.BEAT);
			beatImpress.setSongId(sid);
			impressDao.save(beatImpress);
			return !Strings.isNullOrEmpty(beatImpress.getId());
		}
		if (Dimension.FU_DIAN.equals(dimension)) {
			FuDianImpress fuDianImpress = new FuDianImpress();
			Boolean hasFuDian = (Boolean) map.getOrDefault("fu_dian", false);
			fuDianImpress.setHasFuDian(hasFuDian);
			fuDianImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			fuDianImpress.setDimension(Dimension.FU_DIAN);
			fuDianImpress.setSongId(sid);
			impressDao.save(fuDianImpress);
			return !Strings.isNullOrEmpty(fuDianImpress.getId());
		}
		if (Dimension.INTERVAL.equals(dimension)) {
			IntervalImpress intervalImpress = new IntervalImpress();
			String inter = (String) map.getOrDefault("interval", "");
			String notea = (String) map.getOrDefault("notea", "");
			String noteb = (String) map.getOrDefault("noteb", "");
			intervalImpress.setIntervals(inter);
			intervalImpress.setNotea(notea);
			intervalImpress.setNoteb(noteb);
			intervalImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			intervalImpress.setDimension(Dimension.INTERVAL);
			intervalImpress.setSongId(sid);
			impressDao.save(intervalImpress);
			return !Strings.isNullOrEmpty(intervalImpress.getId());
		}
		if (Dimension.MELODY_LINE.equals(dimension)) {
			MelodyLineImpress melodyLineImpress = new MelodyLineImpress();
			Boolean hasMelodyLine = (Boolean) map.getOrDefault("melody_line", false);
			melodyLineImpress.setHasMelodyLine(hasMelodyLine);
			melodyLineImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			melodyLineImpress.setDimension(Dimension.MELODY_LINE);
			melodyLineImpress.setSongId(sid);
			impressDao.save(melodyLineImpress);
			return !Strings.isNullOrEmpty(melodyLineImpress.getId());
		}
		if (Dimension.MODE.equals(dimension)) {
			ModeImpress modeImpress = new ModeImpress();
			String keySig = (String) map.getOrDefault("keysig", "");
			String type = (String) map.getOrDefault("type", "");
			String contentKeyNote = (String) map.getOrDefault("content_keynote", "");
			String contentMode = (String) map.getOrDefault("content_mode", "");
			modeImpress.setKeySig(keySig);
			modeImpress.setType(type);
			modeImpress.setContentKeyNote(contentKeyNote);
			modeImpress.setContentMode(contentMode);
			modeImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			modeImpress.setDimension(Dimension.MODE);
			modeImpress.setSongId(sid);
			impressDao.save(modeImpress);
			return !Strings.isNullOrEmpty(modeImpress.getId());
		}
		if (Dimension.MUSICAL_FORM.equals(dimension)) {
			MusicalFormImpress musicalFormImpress = new MusicalFormImpress();
			String text = (String) map.getOrDefault("text", "");
			Number startBar = (Number) map.getOrDefault("start_bar", null);
			Number endBar = (Number) map.getOrDefault("end_bar", null);
			musicalFormImpress.setText(text);
			musicalFormImpress.setStartBar(null == startBar ? null : startBar.intValue());
			musicalFormImpress.setEndBar(null == endBar ? null : endBar.intValue());
			musicalFormImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			musicalFormImpress.setDimension(Dimension.MUSICAL_FORM);
			musicalFormImpress.setSongId(sid);
			impressDao.save(musicalFormImpress);
			return !Strings.isNullOrEmpty(musicalFormImpress.getId());
		}
		if (Dimension.MUSICAL_FORM_DESC.equals(dimension)) {
			MusicalFormDescImpress musicalFormDescImpress = new MusicalFormDescImpress();
			String musicalFormsDesc = (String) map.getOrDefault("musical_form_desc", null);
			musicalFormDescImpress.setMusicalFormDesc(musicalFormsDesc);
			musicalFormDescImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			musicalFormDescImpress.setDimension(Dimension.MUSICAL_FORM_DESC);
			musicalFormDescImpress.setSongId(sid);
			impressDao.save(musicalFormDescImpress);
			return !Strings.isNullOrEmpty(musicalFormDescImpress.getId());
		}
		if (Dimension.PITCH.equals(dimension)) {
			PitchImpress pitchImpress = new PitchImpress();
			String pitch = (String) map.getOrDefault("pitch", "");
			pitchImpress.setPitch(pitch);
			pitchImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			pitchImpress.setDimension(Dimension.PITCH);
			pitchImpress.setSongId(sid);
			impressDao.save(pitchImpress);
			return !Strings.isNullOrEmpty(pitchImpress.getId());
		}
		if (Dimension.RHYTHM.equals(dimension)) {
			RhythmImpress thythmImpress = new RhythmImpress();
			String rhythm = (String) map.getOrDefault("rhythm", "");
			thythmImpress.setRhythm(rhythm);
			thythmImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			thythmImpress.setDimension(Dimension.RHYTHM);
			thythmImpress.setSongId(sid);
			impressDao.save(thythmImpress);
			return !Strings.isNullOrEmpty(thythmImpress.getId());
		}
		if (Dimension.RUO_QI.equals(dimension)) {
			RuoQiImpress ruoQiImpress = new RuoQiImpress();
			Boolean hasRuoQi = (Boolean) map.getOrDefault("ruo_qi", false);
			ruoQiImpress.setHasRuoQi(hasRuoQi);
			ruoQiImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			ruoQiImpress.setDimension(Dimension.RUO_QI);
			ruoQiImpress.setSongId(sid);
			impressDao.save(ruoQiImpress);
			return !Strings.isNullOrEmpty(ruoQiImpress.getId());
		}
		if (Dimension.SPEED.equals(dimension)) {
			SpeedImpress speedImpress = new SpeedImpress();
			Number speed = (Number) map.getOrDefault("speed", null);
			speedImpress.setSpeed(null == speed ? null : speed.intValue());
			speedImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			speedImpress.setDimension(Dimension.SPEED);
			speedImpress.setSongId(sid);
			impressDao.save(speedImpress);
			return !Strings.isNullOrEmpty(speedImpress.getId());
		}
		if (Dimension.VOLUME_RANGE.equals(dimension)) {
			VolumeRangeImpress volumeRangeImpress = new VolumeRangeImpress();
			String volumeRange = (String) map.getOrDefault("volume_range", "");
			volumeRangeImpress.setVolumeRange(volumeRange);
			volumeRangeImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
			volumeRangeImpress.setDimension(Dimension.VOLUME_RANGE);
			volumeRangeImpress.setSongId(sid);
			impressDao.save(volumeRangeImpress);
			return !Strings.isNullOrEmpty(volumeRangeImpress.getId());
		}
		if (Dimension.ACCOMPANY.equals(dimension)) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return false;
			}
			AccompanyImpress accompanyImpress = new AccompanyImpress();
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				accompanyImpress.setResourceId(rid);
				accompanyImpress.setSummary(summary);
				accompanyImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
				accompanyImpress.setDimension(Dimension.ACCOMPANY);
				accompanyImpress.setSongId(sid);
				impressDao.save(accompanyImpress);
				return !Strings.isNullOrEmpty(accompanyImpress.getId());
			}
		}
		if (Dimension.MODEL_SINGING.equals(dimension)) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return false;
			}
			ModelSingingImpress modelSingingImpress = new ModelSingingImpress();
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				modelSingingImpress.setResourceId(rid);
				modelSingingImpress.setSummary(summary);
				modelSingingImpress.setField(Field.MIDDLE_PRIMARY_SCHOOL);
				modelSingingImpress.setDimension(Dimension.MODEL_SINGING);
				modelSingingImpress.setSongId(sid);
				impressDao.save(modelSingingImpress);
				return !Strings.isNullOrEmpty(modelSingingImpress.getId());
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> getImpress(String id) {
		Impress impress = impressDao.findByID(id);
		if (null == impress) {
			return null;
		}
		return toJson(impress);
	}

	@Override
	public List<Map<String, Object>> getBySong(String sid) {
		List<Impress> impresses = impressDao.getBySong(sid);
		if (null == impresses || impresses.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> res = Lists.newArrayList();
		impresses.forEach(impress -> {
			res.add(toJson(impress));
		});
		return res;
	}

	@Override
	public void update(String iid, Map<String, Object> map, InputStream in, String filename, String ext)
			throws IOException {
		Impress impress = impressDao.findByID(iid);
		if (null == impress) {
			return;
		}
		if (impress instanceof NationalityAreaImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			NationalityAreaImpress nationalityAreaImpress = (NationalityAreaImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				nationalityAreaImpress.setResourceId(rid);
				nationalityAreaImpress.setSummary(summary);
				impressDao.update(nationalityAreaImpress);
			}
		}
		if (impress instanceof ComposerImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			ComposerImpress composerImpress = (ComposerImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				composerImpress.setResourceId(rid);
				composerImpress.setSummary(summary);
				impressDao.update(composerImpress);
			}
		}
		if (impress instanceof BkgImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			BkgImpress bkgImpress = (BkgImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				bkgImpress.setResourceId(rid);
				bkgImpress.setSummary(summary);
				impressDao.update(bkgImpress);
			}
		}
		if (impress instanceof GraceNoteImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			GraceNoteImpress graceNoteImpress = (GraceNoteImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				graceNoteImpress.setResourceId(rid);
				graceNoteImpress.setSummary(summary);
				impressDao.update(graceNoteImpress);
			}
		}
		if (impress instanceof VigorPlayImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			VigorPlayImpress vigorPlayImpress = (VigorPlayImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				vigorPlayImpress.setResourceId(rid);
				vigorPlayImpress.setSummary(summary);
				impressDao.update(vigorPlayImpress);
			}
		}
		if (impress instanceof TypesLiteratureImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			TypesLiteratureImpress typesLiteratureImpress = (TypesLiteratureImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				typesLiteratureImpress.setResourceId(rid);
				typesLiteratureImpress.setSummary(summary);
				impressDao.update(typesLiteratureImpress);
			}
		}
		if (impress instanceof BeatImpress) {
			BeatImpress beatImpress = (BeatImpress) impress;
			String beat = (String) map.getOrDefault("beat", "");
			beatImpress.setBeat(beat);
			impressDao.update(beatImpress);
		}
		if (impress instanceof FuDianImpress) {
			FuDianImpress fuDianImpress = (FuDianImpress) impress;
			Boolean hasFuDian = (Boolean) map.getOrDefault("fu_dian", false);
			fuDianImpress.setHasFuDian(hasFuDian);
			impressDao.update(fuDianImpress);
		}
		if (impress instanceof IntervalImpress) {
			IntervalImpress intervalImpress = (IntervalImpress) impress;
			String inter = (String) map.getOrDefault("interval", "");
			String notea = (String) map.getOrDefault("notea", "");
			String noteb = (String) map.getOrDefault("noteb", "");
			intervalImpress.setIntervals(inter);
			intervalImpress.setNotea(notea);
			intervalImpress.setNoteb(noteb);
			impressDao.update(intervalImpress);
		}
		if (impress instanceof MelodyLineImpress) {
			MelodyLineImpress melodyLineImpress = (MelodyLineImpress) impress;
			Boolean hasMelodyLine = (Boolean) map.getOrDefault("melody_line", false);
			melodyLineImpress.setHasMelodyLine(hasMelodyLine);
			impressDao.update(melodyLineImpress);
		}
		if (impress instanceof ModeImpress) {
			ModeImpress modeImpress = (ModeImpress) impress;
			String keySig = (String) map.getOrDefault("keysig", "");
			String type = (String) map.getOrDefault("type", "");
			String contentKeyNote = (String) map.getOrDefault("content_keynote", "");
			String contentMode = (String) map.getOrDefault("content_mode", "");
			modeImpress.setKeySig(keySig);
			modeImpress.setType(type);
			modeImpress.setContentKeyNote(contentKeyNote);
			modeImpress.setContentMode(contentMode);
			impressDao.update(modeImpress);
		}
		if (impress instanceof MusicalFormImpress) {
			MusicalFormImpress musicalFormImpress = (MusicalFormImpress) impress;
			String text = (String) map.getOrDefault("text", "");
			Number startBar = (Number) map.getOrDefault("start_bar", null);
			Number endBar = (Number) map.getOrDefault("end_bar", null);
			musicalFormImpress.setText(text);
			musicalFormImpress.setStartBar(null == startBar ? null : startBar.intValue());
			musicalFormImpress.setEndBar(null == endBar ? null : endBar.intValue());
			impressDao.update(musicalFormImpress);
		}
		if (impress instanceof MusicalFormDescImpress) {
			MusicalFormDescImpress musicalFormDescImpress = (MusicalFormDescImpress) impress;
			String musicalFormsDesc = (String) map.getOrDefault("musical_form_desc", null);
			musicalFormDescImpress.setMusicalFormDesc(musicalFormsDesc);
			impressDao.update(musicalFormDescImpress);
		}
		if (impress instanceof PitchImpress) {
			PitchImpress pitchImpress = (PitchImpress) impress;
			String pitch = (String) map.getOrDefault("pitch", "");
			pitchImpress.setPitch(pitch);
			impressDao.update(pitchImpress);
		}
		if (impress instanceof RhythmImpress) {
			RhythmImpress thythmImpress = (RhythmImpress) impress;
			String rhythm = (String) map.getOrDefault("rhythm", "");
			thythmImpress.setRhythm(rhythm);
			impressDao.update(thythmImpress);
		}
		if (impress instanceof RuoQiImpress) {
			RuoQiImpress ruoQiImpress = (RuoQiImpress) impress;
			Boolean hasRuoQi = (Boolean) map.getOrDefault("ruo_qi", false);
			ruoQiImpress.setHasRuoQi(hasRuoQi);
			impressDao.update(ruoQiImpress);
		}
		if (impress instanceof SpeedImpress) {
			SpeedImpress speedImpress = (SpeedImpress) impress;
			Number speed = (Number) map.getOrDefault("speed", null);
			speedImpress.setSpeed(null == speed ? null : speed.intValue());
			impressDao.update(speedImpress);
		}
		if (impress instanceof VolumeRangeImpress) {
			VolumeRangeImpress volumeRangeImpress = (VolumeRangeImpress) impress;
			String volumeRange = (String) map.getOrDefault("volume_range", "");
			volumeRangeImpress.setVolumeRange(volumeRange);
			impressDao.update(volumeRangeImpress);
		}
		if (impress instanceof AccompanyImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			AccompanyImpress accompanyImpress = (AccompanyImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				accompanyImpress.setResourceId(rid);
				accompanyImpress.setSummary(summary);
				impressDao.update(accompanyImpress);
			}
		}
		if (impress instanceof ModelSingingImpress) {
			if (null == in || Strings.isNullOrEmpty(filename) || Strings.isNullOrEmpty(ext)) {
				return;
			}
			ModelSingingImpress modelSingingImpress = (ModelSingingImpress) impress;
			String summary = (String) map.getOrDefault("summary", "");
			Map<String, String> params = Maps.newHashMap();
			params.put("fileName", filename);
			params.put("resourceType", "SONG_RESOURCE");
			String rid = resourceService.save(gson.toJson(params), in, filename, ext);
			if (!Strings.isNullOrEmpty(rid)) {
				modelSingingImpress.setResourceId(rid);
				modelSingingImpress.setSummary(summary);
				impressDao.update(modelSingingImpress);
			}
		}
	}

	@Override
	public boolean delete(String iid) {
		Impress impress = get(iid);
		if (null != impress) {
			impressDao.delete(impress);
			return true;
		}
		return false;
	}

}
