package com.arsoft.projects.thevibgyor.dictionary.controller.superadmin;

import com.arsoft.projects.thevibgyor.backend.model.GenericResponse;
import com.arsoft.projects.thevibgyor.backend.model.GenericResponseInfo;
import com.arsoft.projects.thevibgyor.backend.model.Header;
import com.arsoft.projects.thevibgyor.common.constant.ApiUri;
import com.arsoft.projects.thevibgyor.common.constant.HttpStatus;
import com.arsoft.projects.thevibgyor.common.exception.GenericException;
import com.arsoft.projects.thevibgyor.common.util.HttpRequestUtil;
import com.arsoft.projects.thevibgyor.common.util.TimeUtil;
import com.arsoft.projects.thevibgyor.dictionary.constant.DictionaryApiUri;
import com.arsoft.projects.thevibgyor.dictionary.model.Language;
import com.arsoft.projects.thevibgyor.dictionary.model.Word;
import com.arsoft.projects.thevibgyor.dictionary.service.DictionaryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(ApiUri.SUPER_ADMIN + DictionaryApiUri.DICTIONARY)
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @PostMapping(value = DictionaryApiUri.WORDS)
    public GenericResponse<Word> addWord(@RequestAttribute("requestTime") ZonedDateTime requestTime, HttpServletRequest request, @RequestBody Word word) throws BadRequestException {
        log.info("Request has reached super admin dictionary controller now");
        validateInput(word);
        Header header;
        ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
        String url = HttpRequestUtil.getFullURL(request);
        try {
            dictionaryService.addWord(word);
            GenericResponseInfo<Word> genericResponseInfo = new GenericResponseInfo<>(false,
                    "words", 1, word);
            long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
            header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
            return new GenericResponse<>(header, genericResponseInfo);
        } catch (Exception e) {
            log.error(String.format("Exception occurred %s", e.getMessage()));
            throw new GenericException(e, requestTime);
        }
    }

    @GetMapping(value = DictionaryApiUri.WORDS)
    public GenericResponse<List<Word>> getAllWord(@RequestAttribute("requestTime") ZonedDateTime requestTime, HttpServletRequest request, @RequestParam String letter) throws BadRequestException {
        log.info("Request has reached super admin dictionary controller now inside getAllWord");
        validateInput(letter, 1);
        Header header;
        ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
        String url = HttpRequestUtil.getFullURL(request);
        try {
            List<Word> words = dictionaryService.getAllWords(letter.toUpperCase());
            GenericResponseInfo<List<Word>> genericResponseInfo = new GenericResponseInfo<>(true,
                    "words", words.size(), words);
            long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
            header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
            return new GenericResponse<>(header, genericResponseInfo);
        } catch (Exception e) {
            log.error(String.format("Exception occurred %s", e.getMessage()));
            throw new GenericException(e, requestTime);
        }
    }

    @GetMapping(value = DictionaryApiUri.LANGUAGES)
    public GenericResponse<List<String>> getLanguages(@RequestAttribute("requestTime") ZonedDateTime requestTime, HttpServletRequest request) throws BadRequestException {
        log.info("Request has reached super admin dictionary controller now inside getLanguages");
        Header header;
        ZonedDateTime startZonedDateTime = Objects.requireNonNullElseGet(requestTime, ZonedDateTime::now);
        String url = HttpRequestUtil.getFullURL(request);
        try {
            List<String> languages = Arrays.stream(Language.values())
                    .map(Enum::name)
                    .toList();
            GenericResponseInfo<List<String>> genericResponseInfo = new GenericResponseInfo<>(true,
                    "words", languages.size(), languages);
            long elapsedTime = TimeUtil.getElapsedTimeInMillis(startZonedDateTime, ZonedDateTime.now());
            header = new Header(url, startZonedDateTime, ZonedDateTime.now(), elapsedTime, HttpStatus.OK.getValue());
            return new GenericResponse<>(header, genericResponseInfo);
        } catch (Exception e) {
            log.error(String.format("Exception occurred %s", e.getMessage()));
            throw new GenericException(e, requestTime);
        }
    }

    private void validateInput(String letter, int length) throws BadRequestException {
        if (letter == null || letter.isEmpty() || letter.length() < length) {
            throw new BadRequestException(String.format("The letter %s is not of at least %s length ", letter, length));
        }
    }

    private void validateInput(Word word) throws BadRequestException {
        if (word.getInput() == null || word.getInput().getText() == null || word.getInput().getText().isEmpty()) {
            throw new BadRequestException("Input text is null");
        }
        if (word.getMeanings() == null || word.getMeanings().isEmpty()) {
            throw new BadRequestException("At least one meaning needs to be given");
        }
        AtomicBoolean invalid = new AtomicBoolean(false);
        word.getMeanings().forEach(meaning -> {
            if (meaning.getMeaning() == null || meaning.getMeaning().isEmpty()) {
                invalid.set(true);
                log.error("Invalid meaning provided: " + meaning);
            }
        });
        if (invalid.get()) {
            throw new BadRequestException("At least one of the meaning has invalid text");
        }
    }
}
