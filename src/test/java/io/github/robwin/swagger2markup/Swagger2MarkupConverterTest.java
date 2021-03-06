/*
 *
 *  Copyright 2015 Robert Winkler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package io.github.robwin.swagger2markup;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.robwin.markup.builder.MarkupLanguage;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.BDDAssertions.assertThat;

public class Swagger2MarkupConverterTest {

    @Test
    public void testSwagger2AsciiDocConversion() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(3).containsAll(asList("definitions.adoc", "overview.adoc", "paths.adoc"));
    }


    @Test
    public void testOldSwaggerSpec2AsciiDocConversion() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger_12.json").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(3).containsAll(asList("definitions.adoc", "overview.adoc", "paths.adoc"));
    }

    @Test
    public void testSwagger2AsciiDocConversionWithDescriptionsAndExamples() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).withDescriptions("src/docs/asciidoc")
                .withExamples("src/docs/asciidoc/paths").build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(3).containsAll(asList("definitions.adoc", "overview.adoc", "paths.adoc"));
    }

    @Test
    public void testSwagger2AsciiDocConversionDoesNotContainUriScheme() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/yaml/swagger_should_not_contain_uri_scheme.yaml").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(3).containsAll(asList("definitions.adoc", "overview.adoc", "paths.adoc"));

        assertThat(new String(Files.readAllBytes(Paths.get(outputDirectory + File.separator + "overview.adoc"))))
                .doesNotContain("=== URI scheme");
    }

    @Test
    public void testSwagger2AsciiDocConversionContainsUriScheme() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/yaml/swagger_should_contain_uri_scheme.yaml").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(3).containsAll(asList("definitions.adoc", "overview.adoc", "paths.adoc"));

        assertThat(new String(Files.readAllBytes(Paths.get(outputDirectory + File.separator + "overview.adoc"))))
                .contains("=== URI scheme");
    }

    @Test
    public void testSwagger2MarkdownConversion() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        File outputDirectory = new File("build/docs/markdown/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).
                withMarkupLanguage(MarkupLanguage.MARKDOWN).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(3).containsAll(asList("definitions.md", "overview.md", "paths.md"));
    }

    @Test
    public void testSwagger2MarkdownConversionWithDescriptions() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        File outputDirectory = new File("build/docs/markdown/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).withDescriptions("src/docs/markdown").
                withMarkupLanguage(MarkupLanguage.MARKDOWN).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(3).containsAll(asList("definitions.md", "overview.md", "paths.md"));
    }

    @Test
    public void testSwagger2AsciiDocConversionWithSeparatedDefinitions() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).withSeparatedDefinitions().build()
            .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(9).containsAll(
            asList("definitions.adoc", "overview.adoc", "paths.adoc", "identified.adoc",
                "user.adoc", "category.adoc", "pet.adoc", "tag.adoc", "order.adoc"));
        assertThat(new String(Files.readAllBytes(Paths.get(outputDirectory + File.separator + "definitions.adoc"))))
            .contains(new String(Files.readAllBytes(Paths.get(outputDirectory + File.separator + "user.adoc"))));
    }

    @Test
    public void testSwagger2MarkdownConversionWithSeparatedDefinitions() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).withSeparatedDefinitions().
                withMarkupLanguage(MarkupLanguage.MARKDOWN).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        //Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(9).containsAll(
                asList("definitions.md", "overview.md", "paths.md", "identified.md",
                        "user.md", "category.md", "pet.md", "tag.md", "order.md"));
        assertThat(new String(Files.readAllBytes(Paths.get(outputDirectory + File.separator + "definitions.md"))))
                .contains(new String(Files.readAllBytes(Paths.get(outputDirectory + File.separator + "user.md"))));
    }

    @Test
    public void testSwagger2MarkdownConversionHandlesComposition() throws IOException {
        //Given
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        File outputDirectory = new File("build/docs/asciidoc/generated");
        FileUtils.deleteQuietly(outputDirectory);

        //When
        Swagger2MarkupConverter.from(file.getAbsolutePath()).withSeparatedDefinitions().
                withMarkupLanguage(MarkupLanguage.MARKDOWN).build()
                .intoFolder(outputDirectory.getAbsolutePath());

        // Then
        String[] directories = outputDirectory.list();
        assertThat(directories).hasSize(9).containsAll(
                asList("definitions.md", "overview.md", "paths.md", "identified.md",
                        "user.md", "category.md", "pet.md", "tag.md", "order.md"));
        verifyMarkdownContainsFieldsInTables(
                outputDirectory + File.separator + "definitions.md",
                ImmutableMap.<String, Set<String>>builder()
                        .put("Identified", ImmutableSet.of("id"))
                        .put("User", ImmutableSet.of("id", "username", "firstName",
                                "lastName", "email", "password", "phone", "userStatus"))
                        .build());
        verifyMarkdownContainsFieldsInTables(
                outputDirectory + File.separator + "user.md",
                ImmutableMap.<String, Set<String>>builder()
                        .put("User", ImmutableSet.of("id", "username", "firstName",
                                "lastName", "email", "password", "phone", "userStatus"))
                        .build()
        );

    }

    /**
     * Given a markdown document to search, this checks to see if the specified tables
     * have all of the expected fields listed.
     *
     * @param doc path of markdown document to inspect
     * @param fieldsByTable map of table name (header) to field names expected
     *                      to be found in that table.
     * @throws IOException if the markdown document could not be read
     */
    private static void verifyMarkdownContainsFieldsInTables(String doc, Map<String, Set<String>> fieldsByTable) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get(doc), Charset.defaultCharset());
        final Map<String, Set<String>> fieldsLeftByTable = Maps.newHashMap();
        for(Map.Entry<String, Set<String>> entry : fieldsByTable.entrySet()) {
            fieldsLeftByTable.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
        }
        String inTable = null;
        for(String line : lines) {
            // If we've found every field we care about, quit early
            if(fieldsLeftByTable.isEmpty()) {
                return;
            }

            // Transition to a new table if we encounter a header
            final String currentHeader = getTableHeader(line);
            if(inTable == null || currentHeader != null) {
                inTable = currentHeader;
            }

            // If we're in a table that we care about, inspect this potential table row
            if (inTable != null && fieldsLeftByTable.containsKey(inTable)){
                // If we're still in a table, read the row and check for the field name
                //  NOTE: If there was at least one pipe, then there's at least 2 fields
                String[] parts = line.split("\\|");
                if(parts.length > 1) {
                    final String fieldName = parts[1];
                    final Set<String> fieldsLeft = fieldsLeftByTable.get(inTable);
                    // Mark the field as found and if this table has no more fields to find,
                    //  remove it from the "fieldsLeftByTable" map to mark the table as done
                    if(fieldsLeft.remove(fieldName) && fieldsLeft.isEmpty()) {
                        fieldsLeftByTable.remove(inTable);
                    }
                }
            }
        }

        // After reading the file, if there were still types, fail
        if(!fieldsLeftByTable.isEmpty()) {
            fail(String.format("Markdown file '%s' did not contain expected fields (by table): %s",
                    doc, fieldsLeftByTable));
        }
    }

    private static String getTableHeader(String line) {
        return line.startsWith("###")
                ? line.replace("###", "").trim()
                : null;
    }

    /*
    @Test
    public void testSwagger2HtmlConversion() throws IOException {
        File file = new File(Swagger2MarkupConverterTest.class.getResource("/json/swagger.json").getFile());
        String asciiDoc =  Swagger2MarkupConverter.from(file.getAbsolutePath()).build().asString();
        String path = "build/docs/generated/asciidocAsString";
        Files.createDirectories(Paths.get(path));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path, "swagger.adoc"), StandardCharsets.UTF_8)){
            writer.write(asciiDoc);        }
        String asciiDocAsHtml = Asciidoctor.Factory.create().convert(asciiDoc,
                OptionsBuilder.options().backend("html5").headerFooter(true).safe(SafeMode.UNSAFE).docType("book").attributes(AttributesBuilder.attributes()
                        .tableOfContents(true).tableOfContents(Placement.LEFT).sectionNumbers(true).hardbreaks(true).setAnchors(true).attribute("sectlinks")));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path, "swagger.html"), StandardCharsets.UTF_8)){
            writer.write(asciiDocAsHtml);
        }
    }
    */
}
