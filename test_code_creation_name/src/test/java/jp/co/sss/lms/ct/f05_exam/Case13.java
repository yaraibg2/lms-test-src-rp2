package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
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

/**
 * 結合テスト 試験実施機能
 * ケース13
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果0点")
public class Case13 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
						"evidence/case13/01_loginPage.png"));
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
						"evidence/case13/02_success.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws Exception {
		final List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement examDay = null;

		for (WebElement element : elements) {
			scrollBy("50");
			final List<WebElement> classElements = element.findElements(By.className("w10per"));
			if (classElements.get(1).getText().equals("試験有")) {
				examDay = element.findElement(By.tagName("form"));
				break;
			}
		}
		examDay.click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("セクション詳細 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case13/03_section.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() throws Exception {
		webDriver.findElement(By.xpath("//*[@id=\"sectionDetail\"]/table[1]/tbody/tr[2]/td[2]/form/input[1]")).click();
		pageLoadTimeout(20);

		String pageTitle = webDriver.getTitle();
		assertEquals("試験【ITリテラシー①】 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case13/04_examDetail.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() throws Exception {
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div/form/input[4]")).click();
		pageLoadTimeout(20);

		String pageTitle = webDriver.getTitle();
		assertEquals("ITリテラシー① | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case13/05_examStart.png"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 未回答の状態で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() throws Exception {
		// タイマーが出るまでの時間稼ぎ
		for (int i = 0; i <= 50; i++) {
			scrollTo("0");
		}
		scrollBy("10000");

		webDriver.findElement(By.xpath("//*[@id=\"examQuestionForm\"]/div[13]/fieldset/input")).click();
		pageLoadTimeout(20);

		String url = webDriver.getCurrentUrl();
		assertEquals("http://localhost:8080/lms/exam/answerCheck", url);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case13/06_examCheck.png"));
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException, IOException {
		// タイマーが出るまでの時間稼ぎ
		for (int i = 0; i <= 50; i++) {
			scrollTo("0");
		}
		scrollBy("10000");

		webDriver.findElement(By.xpath("//*[@id=\"sendButton\"]")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();
		date = new Date();
		pageLoadTimeout(20);

		String url = webDriver.getCurrentUrl();
		assertEquals("http://localhost:8080/lms/exam/result", url);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case13/07_examResult.png"));
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() throws Exception {
		scrollBy("10000");

		webDriver.findElement(By.xpath("//*[@id=\"examBeing\"]/div[13]/fieldset/form/input[1]")).click();
		pageLoadTimeout(20);
		scrollBy("100");
		pageLoadTimeout(20);

		List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement lastExam = elements.getLast();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
		String examDate = sdf.format(date);

		assertEquals("0.0点", lastExam.findElements(By.tagName("td")).get(1).getText());
		assertEquals(examDate, lastExam.findElements(By.tagName("td")).get(3).getText());

		String pageTitle = webDriver.getTitle();
		assertEquals("試験【ITリテラシー①】 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case13/08_examComplete.png"));
	}

}
