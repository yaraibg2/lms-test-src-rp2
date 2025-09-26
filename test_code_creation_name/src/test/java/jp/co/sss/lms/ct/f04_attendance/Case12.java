package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() throws Exception {
		goTo("http://localhost:8080/lms/");
		String pageTitle = webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/01_loginPage.png"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws Exception {
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA01");
		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("tisUserAA01");

		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[3]/div/input")).click();
		pageLoadTimeout(20);
		String pageTitle = webDriver.getTitle();
		assertEquals("コース詳細 | LMS", pageTitle);
		String welcomeMsg = webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[2]/a/small")).getText();
		assertEquals("ようこそ受講生ＡＡ１さん", welcomeMsg);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/02_success.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() throws Exception {
		webDriver.findElement(By.linkText("勤怠")).click();

		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		String pageTitle = webDriver.getTitle();
		assertEquals("勤怠情報変更｜LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/03_attendance.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() throws Exception {
		webDriver.findElement(By.linkText("勤怠情報を直接編集する")).click();

		pageLoadTimeout(20);
		String url = webDriver.getCurrentUrl();
		assertEquals("http://localhost:8080/lms/attendance/update", url);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/04_directAttendance.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() throws Exception {
		final List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement yesterday = elements.get(1);
		WebElement today = elements.get(2);
		final List<WebElement> yesterdayTimes = yesterday.findElements(By.tagName("select"));
		final List<WebElement> todayTimes = today.findElements(By.tagName("select"));

		Select yStartHour = new Select(yesterdayTimes.get(0));
		yStartHour.selectByVisibleText("09");
		Select yStartMinute = new Select(yesterdayTimes.get(1));
		yStartMinute.selectByVisibleText("");

		Select yEndHour = new Select(yesterdayTimes.get(2));
		yEndHour.selectByVisibleText("");
		Select yEndMinute = new Select(yesterdayTimes.get(3));
		yEndMinute.selectByVisibleText("00");

		Select tStartHour = new Select(todayTimes.get(0));
		tStartHour.selectByVisibleText("");
		Select tStartMinute = new Select(todayTimes.get(1));
		tStartMinute.selectByVisibleText("00");

		Select tEndHour = new Select(todayTimes.get(2));
		tEndHour.selectByVisibleText("18");
		Select tEndMinute = new Select(todayTimes.get(3));
		tEndMinute.selectByVisibleText("");

		scrollBy("700");
		webDriver.findElement(By.name("complete")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		pageLoadTimeout(20);

		final List<WebElement> newElements = webDriver.findElements(By.tagName("tr"));
		WebElement newYesterday = newElements.get(1);
		WebElement newToday = newElements.get(2);
		final List<WebElement> newYesterdayTimes = newYesterday.findElements(By.tagName("select"));
		final List<WebElement> newTodayTimes = newToday.findElements(By.tagName("select"));

		assertEquals("form-control errorInput", newYesterdayTimes.get(1).getAttribute("class"));
		assertEquals("form-control errorInput", newYesterdayTimes.get(2).getAttribute("class"));
		assertEquals("form-control errorInput", newTodayTimes.get(0).getAttribute("class"));
		assertEquals("form-control errorInput", newTodayTimes.get(3).getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/05_hourAndMinuteError.png"));

		WebElement table = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div"));
		final List<WebElement> buttons = table.findElements(By.tagName("button"));
		for (WebElement button : buttons) {
			button.click();
		}
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() throws Exception {
		final List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement yesterday = elements.get(1);
		final List<WebElement> yesterdayTimes = yesterday.findElements(By.tagName("select"));

		Select startHour = new Select(yesterdayTimes.get(0));
		startHour.selectByVisibleText("");
		Select startMinute = new Select(yesterdayTimes.get(1));
		startMinute.selectByVisibleText("");

		Select endHour = new Select(yesterdayTimes.get(2));
		endHour.selectByVisibleText("19");
		Select endMinute = new Select(yesterdayTimes.get(3));
		endMinute.selectByVisibleText("00");

		scrollBy("700");
		webDriver.findElement(By.name("complete")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		pageLoadTimeout(20);

		final List<WebElement> newElements = webDriver.findElements(By.tagName("tr"));
		WebElement newYesterday = newElements.get(1);
		final List<WebElement> newYesterdayTimes = newYesterday.findElements(By.tagName("select"));

		assertEquals("form-control errorInput", newYesterdayTimes.get(0).getAttribute("class"));
		assertEquals("form-control errorInput", newYesterdayTimes.get(1).getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/06_nonStartTimeError.png"));

		WebElement table = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div"));
		final List<WebElement> buttons = table.findElements(By.tagName("button"));
		for (WebElement button : buttons) {
			button.click();
		}
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() throws Exception {
		final List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement yesterday = elements.get(1);
		final List<WebElement> yesterdayTimes = yesterday.findElements(By.tagName("select"));

		Select startHour = new Select(yesterdayTimes.get(0));
		startHour.selectByVisibleText("14");
		Select startMinute = new Select(yesterdayTimes.get(1));
		startMinute.selectByVisibleText("00");

		Select endHour = new Select(yesterdayTimes.get(2));
		endHour.selectByVisibleText("13");
		Select endMinute = new Select(yesterdayTimes.get(3));
		endMinute.selectByVisibleText("00");

		scrollBy("700");
		webDriver.findElement(By.name("complete")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		pageLoadTimeout(20);

		final List<WebElement> newElements = webDriver.findElements(By.tagName("tr"));
		WebElement newYesterday = newElements.get(1);
		final List<WebElement> newYesterdayTimes = newYesterday.findElements(By.tagName("select"));

		assertEquals("form-control errorInput", newYesterdayTimes.get(2).getAttribute("class"));
		assertEquals("form-control errorInput", newYesterdayTimes.get(3).getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/07_startTimeAfterEndTimeError.png"));

		WebElement table = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div"));
		final List<WebElement> buttons = table.findElements(By.tagName("button"));
		for (WebElement button : buttons) {
			button.click();
		}
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() throws Exception {
		final List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement yesterday = elements.get(1);
		final List<WebElement> yesterdayTimes = yesterday.findElements(By.tagName("select"));

		Select startHour = new Select(yesterdayTimes.get(0));
		startHour.selectByVisibleText("10");
		Select startMinute = new Select(yesterdayTimes.get(1));
		startMinute.selectByVisibleText("00");

		Select endHour = new Select(yesterdayTimes.get(2));
		endHour.selectByVisibleText("12");
		Select endMinute = new Select(yesterdayTimes.get(3));
		endMinute.selectByVisibleText("00");

		Select blankTime = new Select(yesterdayTimes.get(4));
		blankTime.selectByVisibleText("3時間");

		scrollBy("700");
		webDriver.findElement(By.name("complete")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		pageLoadTimeout(20);

		final List<WebElement> newElements = webDriver.findElements(By.tagName("tr"));
		WebElement newYesterday = newElements.get(1);
		final List<WebElement> newYesterdayTimes = newYesterday.findElements(By.tagName("select"));

		assertEquals("form-control errorInput", newYesterdayTimes.get(4).getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/08_overBlankTimeError.png"));

		WebElement table = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/div"));
		final List<WebElement> buttons = table.findElements(By.tagName("button"));
		for (WebElement button : buttons) {
			button.click();
		}
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() throws Exception {
		final List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement yesterday = elements.get(1);
		final WebElement note = yesterday.findElements(By.tagName("input")).getLast();
		note.clear();
		note.sendKeys(StringUtils.repeat("研修", 51));

		scrollBy("700");
		webDriver.findElement(By.name("complete")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		pageLoadTimeout(20);

		final List<WebElement> newElements = webDriver.findElements(By.tagName("tr"));
		WebElement newYesterday = newElements.get(1);
		WebElement newNote = newYesterday.findElements(By.tagName("input")).getLast();

		assertEquals("form-control errorInput", newNote.getAttribute("class"));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case12/09_overNoteError.png"));
	}

}
