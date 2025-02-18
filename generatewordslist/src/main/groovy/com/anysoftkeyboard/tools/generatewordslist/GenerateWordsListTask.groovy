/*
 * Copyright (C) 2016 AnySoftKeyboard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anysoftkeyboard.tools.generatewordslist;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jsoup.Jsoup

/**
 * Task to generate words-list XML file from a input
 */
public class GenerateWordsListTask extends DefaultTask {
    public enum InputFileType {
        Text,
        Html
    }

    File inputFile;
    InputFileType inputFileType = InputFileType.Text
    File outputWordsListFile;

    char[] wordCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray()
    char[] additionalInnerCharacters = "'".toCharArray()

    Locale locale = Locale.US

    int maxWordsInList = Integer.MAX_VALUE

    @TaskAction
    def generateWordsList() {
        File wordsInputFile = inputFile
        if (inputFileType.equals(InputFileType.Html)) {
            wordsInputFile = File.createTempFile("stripped_html_", ".txt")
            String inputText = Jsoup.parse(inputFile, "UTF-8").text()
            FileWriter writer = new FileWriter(wordsInputFile)
            writer.write(inputText)
            writer.flush()
            writer.close()
        }
        Parser parser = new Parser(wordsInputFile, outputWordsListFile, wordCharacters, locale, additionalInnerCharacters, maxWordsInList)
        parser.parse()
    }
}