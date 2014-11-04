// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.github.fscheffer.arras.demo;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import com.github.fscheffer.arras.test.ArrasTestCase;

public class DataTableIT extends ArrasTestCase {

    private static final Logger logger          = LoggerFactory.getLogger(DataTableIT.class);

    private static final By     ROWS_PER_PAGE   = By.cssSelector(".dataTables_length select");

    private static final By     FILTER          = By.cssSelector(".dataTables_filter input");

    private static final By     HEADER_CELLS    = By.cssSelector(".dataTable > thead > tr > th");

    private static final By     ROWS            = By.cssSelector(".dataTable > tbody > tr");

    private static final By     PAGINATION_INFO = By.cssSelector(".dataTables_info");

    private String              pagename;

    @Factory(dataProvider = "getPageNames")
    public DataTableIT(String pagename) {
        this.pagename = pagename;
    }

    @DataProvider
    public static Object[][] getPageNames() {
        return new Object[][] { { "/DataTablesDemo" }, { "/DataTablesAjaxDemo" } };
    }

    @BeforeMethod
    void before() {

        open(this.pagename);
        waitUntilInvisible(By.cssSelector(".datatable_processing"));
    }

    @Test
    void testRowsPerPage() {

        assertRowsPerPage(25);

        select(ROWS_PER_PAGE, "50");
        assertRowsPerPage(50);

        select(ROWS_PER_PAGE, "100");
        assertRowsPerPage(100);

        select(ROWS_PER_PAGE, "200");
        assertRowsPerPage(200);

        click(pagination(1));
        assertPaginationInfo(201, 400);
        assertRowCount(200);
    }

    private void assertRowsPerPage(int rowsPerPage) {

        assertTableHeader("Title", "Album", "Artist", "Genre", "Play Count", "Rating");
        assertRowCount(rowsPerPage);

        assertRow(0, "(untitled hidden track)", "Fear Of Fours", "Lamb", "Downtempo", "2", "0");

        if (rowsPerPage >= 25) {
            assertRow(24, "A Million Colors", "Rave 'Til Dawn", "Channel X", "Electronica/Dance", "4", "0");
        }

        if (rowsPerPage >= 50) {
            assertRow(49, "Adia", "Surfacing", "Sarah McLachlan", "Rock/Pop", "3", "0");
        }

        if (rowsPerPage >= 100) {
            assertRow(99, "Anthem", "Everything Is Wrong", "Moby", "Electronica/Dance", "4", "0");
        }

        if (rowsPerPage >= 200) {
            assertRow(199, "Boom (The Crystal Method Remix)", "Community Service", "P.O.D.", "Electronic", "9", "80");
        }

        assertPaginationInfo(1, rowsPerPage);
    }

    @Test
    void testSearch() {

        setFilter("ghost");

        sleep(500);

        assertRowCount(2);
        assertTextPresent(PAGINATION_INFO, "Showing 1 to 2 of 2 entries (filtered from 1,722 total entries)");
        assertRow(0, "Born as Ghosts", "The Battle of Los Angeles", "Rage Against The Machine", "Alternative Metal",
                  "0", "0");
        assertRow(1, "Warboys", "Stolar Tracks Vol. 2", "Ghost Of An American Airman", "Alternative & Punk", "3", "0");

        // workaround: clearing the field is not enough. have to send a key
        setFilter(Keys.ENTER);

        sleep(500);

        assertRowCount(25);
        assertRow(0, "(untitled hidden track)", "Fear Of Fours", "Lamb", "Downtempo", "2", "0");

        // try a filter with paging
        setFilter("Electronic");

        sleep(500);

        assertRowCount(25);
        assertTextPresent(PAGINATION_INFO, "Showing 1 to 25 of 654 entries (filtered from 1,722 total entries)");
        assertRow(0, "04 04 04 04 04 Cowgirl (Irish Pub In Ky", "Dirty Epic - Cowgirl (EP)", "Underworld",
                  "Electronica", "4", "0");

        click(pagination(1));
        assertRowCount(25);
        assertTextPresent(PAGINATION_INFO, "Showing 26 to 50 of 654 entries (filtered from 1,722 total entries)");
        assertRow(0, "Air Batucada", "The Mirror Conspiracy", "Thievery Corporation", "Electronic", "3", "0");

        setFilter("foobar");
        assertRow(0, "No matching records found");
        assertTextPresent(PAGINATION_INFO, "Showing 0 to 0 of 0 entries (filtered from 1,722 total entries)");
    }

    private void setFilter(CharSequence term) {

        text(FILTER, term);

        // wait until DataTable has finished processing
        waitUntilInvisible(By.cssSelector(".datatable_processing"));
    }

    @Test
    void testSorting() {

        // assume first column is sorted ascending by default
        assertPaginationInfo(1, 25);
        assertRow(0, "(untitled hidden track)", "Fear Of Fours", "Lamb", "Downtempo", "2", "0");
        assertRow(1, "04 04 04 04 04 Cowgirl (Irish Pub In Ky", "Dirty Epic - Cowgirl (EP)", "Underworld",
                  "Electronica", "4", "0");
        assertRow(2, "1.618", "This Binary Universe", "BT", "Electronic", "2", "0");

        // second page
        click(pagination(1));

        assertPaginationInfo(26, 50);
        assertRow(0, "A Mistake", "When the Pawn Hits the Conflicts He Thinks Like a King...", "Fiona Apple", "Rock",
                  "6", "40");
        assertRow(1, "A Nervous Tic Motion Of The Head To The Left", "& The Mysterious Producton Of Eggs",
                  "Andrew Bird", "Pop", "1", "0");
        assertRow(2, "A Number Of Microphones", "Decksandrumsandrockandroll", "Propellerheads", "Electronica/Dance",
                  "3", "0");

        // test "title" sorted descending
        click(tableHeader(0));

        assertPaginationInfo(1, 25);
        assertRow(0, "| SUMMER 4. Allegro", "The Four Seasons", "Antonio Vivaldi", "Classical", "1", "0");
        assertRow(1, "| SPRING 1. Allegro", "The Four Seasons", "Antonio Vivaldi", "Classical", "1", "0");
        assertRow(2, "| 9. Allegro", "The Four Seasons", "Antonio Vivaldi", "Classical", "3", "0");
        assertRow(3, "| 8. Adagio", "The Four Seasons", "Antonio Vivaldi", "Classical", "2", "0");

        // test "play count" sorting (ascending)
        click(tableHeader(4));

        assertPaginationInfo(1, 25);

        // we can't check rows here because lots of rows have the same value and the actual order dependes on the
        // underlying algorithm which differs in java from javascript's implementation
        for (int i = 0; i < 25; i++) {
            assertCell(i, 4, "0");
        }

        // test "play count" sorting (descending)
        click(tableHeader(4));

        assertPaginationInfo(1, 25);
        assertRow(0, "Loops Of Fury", "Wipeout 2097", "The Chemical Brothers", "Soundtrack", "40", "80");
        assertRow(1, "Never Enough (Big Mix)", "Mixed Up", "The Cure", "Alternative", "37", "80");
        assertRow(2, "The Lighthouse", "Chaos Theory: Splinter Cell 3 (Soundtrack from the Video Game)", "Amon Tobin",
                  "Soundtrack", "35", "80");

        // try to sort "link" column. expect NOP (i.e. the same rows as above)
        click(tableHeader(6));

        assertPaginationInfo(1, 25);
        assertRow(0, "Loops Of Fury", "Wipeout 2097", "The Chemical Brothers", "Soundtrack", "40", "80");
        assertRow(1, "Never Enough (Big Mix)", "Mixed Up", "The Cure", "Alternative", "37", "80");
        assertRow(2, "The Lighthouse", "Chaos Theory: Splinter Cell 3 (Soundtrack from the Video Game)", "Amon Tobin",
                  "Soundtrack", "35", "80");
    }

    private void assertPaginationInfo(int from, int to) {

        // wait until DataTable has finished processing
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".datatable_processing")));

        assertTextPresent(PAGINATION_INFO, "Showing " + from + " to " + to + " of 1,722 entries");
    }

    private By pagination(int i) {
        return By.cssSelector(".dataTables_paginate > span > a:nth-child(" + (i + 1) + ")");
    }

    private By tableHeader(int i) {
        return By.cssSelector(".dataTable > thead > tr > th:nth-child(" + (i + 1) + ")");
    }

    private void assertRowCount(int expected) {
        Assert.assertEquals(elements(ROWS).size(), expected);
    }

    private void assertTableHeader(String... values) {

        List<WebElement> cells = elements(HEADER_CELLS);

        assertCells(cells, values);
    }

    private void assertRow(int n, String... values) {

        List<WebElement> cells = elements(By.cssSelector(".dataTable > tbody > tr:nth-child(" + (n + 1) + ") > td"));

        assertCells(cells, values);
    }

    private void assertCells(List<WebElement> cells, String... values) {

        for (int i = 0; i < values.length; i++) {

            WebElement cell = cells.get(i);
            Assert.assertEquals(cell.getText(), values[i]);
        }
    }

    private void assertCell(int n, int c, String value) {

        By by = By.cssSelector(".dataTable > tbody > tr:nth-child(" + (n + 1) + ") > td:nth-child(" + (c + 1) + ")");
        assertTextPresent(by, value);
    }
}
