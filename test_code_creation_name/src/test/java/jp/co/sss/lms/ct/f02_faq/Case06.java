package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
						"evidence/case06_01_loginPage.png"));
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
						"evidence/case06_02_success.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() throws Exception {
		pageLoadTimeout(20);
		webDriver.findElement(By.linkText("機能")).click();
		webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[1]/li[4]/ul/li[4]/a")).click();
		pageLoadTimeout(20);
		String pageTitle = webDriver.getTitle();
		assertEquals("ヘルプ | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case06_03_helpPage.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() throws Exception {
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[2]/p/a")).click();
		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		webDriver.switchTo().window((String) windowHandles[1]);
		String pageTitle = webDriver.getTitle();
		assertEquals("よくある質問 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case06_04_question.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() throws Exception {
		final List<WebElement> allElements = webDriver.findElements(By.className("sorting_1"));
		int categoryElements = 0;

		webDriver.findElement(By.linkText("【研修関係】")).click();
		scrollBy("300");
		final List<WebElement> trainingElements = webDriver.findElements(By.className("sorting_1"));
		categoryElements += trainingElements.size();
		scrollTo("0");

		webDriver.findElement(By.linkText("【人材開発支援助成金】")).click();
		scrollBy("300");
		final List<WebElement> moneyElements = webDriver.findElements(By.className("sorting_1"));
		categoryElements += moneyElements.size();
		scrollTo("20");

		webDriver.findElement(By.linkText("【遠隔研修】")).click();
		scrollBy("300");
		final List<WebElement> remoteElements = webDriver.findElements(By.className("sorting_1"));
		categoryElements += remoteElements.size();
		scrollTo("0");

		assertEquals(allElements.size(), categoryElements);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case06_05_category.png"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() throws Exception {
		webDriver.findElement(By.linkText("【研修関係】")).click();
		scrollBy("300");
		final List<WebElement> elements = webDriver.findElements(By.className("sorting_1"));
		WebElement element = elements.get(0);
		String beforeText = element.getText();
		element.click();
		String afterText = element.getText();

		assertFalse(beforeText.equals(afterText));
		assertTrue(beforeText.length() < afterText.length());

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case06_06_answer.png"));
	}

}
