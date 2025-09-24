package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
						"evidence/case8_1_loginPage.png"));
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
						"evidence/case8_2_success.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws Exception {
		final List<WebElement> elements = webDriver.findElements(By.tagName("tr"));
		WebElement weekReportDay = elements.get(1).findElement(By.tagName("form"));
		weekReportDay.click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("セクション詳細 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case8_3_section.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws Exception {
		scrollBy("300");
		webDriver.findElement(By.xpath("//*[@id=\"sectionDetail\"]/table[2]/tbody/tr[3]/td/form/input[6]")).click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("レポート登録 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case8_4_report.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() throws Exception {
		Select understanding = new Select(webDriver.findElement(By.xpath("//*[@id=\"intFieldValue_0\"]")));
		understanding.selectByVisibleText("3");

		WebElement goal = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		goal.clear();
		goal.sendKeys("10");

		WebElement comment = webDriver.findElement(By.xpath("//*[@id=\"content_1\"]"));
		comment.clear();
		comment.sendKeys("今週も楽しく学べました。");

		WebElement textArea = webDriver.findElement(By.xpath("//*[@id=\"content_2\"]"));
		textArea.clear();
		textArea.sendKeys("この一週間で、かなり成長できた気がします。");
		scrollBy("100");

		webDriver.findElement(By.xpath("//*[@id=\"main\"]/form/div[3]/fieldset/div/div/button")).click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("セクション詳細 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case8_5_submit.png"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() throws Exception {
		webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[2]/a")).click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("ユーザー詳細", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case8_6_userDetail.png"));

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() throws Exception {
		scrollBy("700");
		final List<WebElement> elements = webDriver.findElements(By.tagName("form"));
		WebElement element = elements.get(elements.size() - 9);

		element.click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("レポート詳細 | LMS", pageTitle);
		String understanding = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/table/tbody/tr[2]/td[2]/p"))
				.getText();
		assertEquals("3", understanding);
		String goal = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[1]/td")).getText();
		assertEquals("10", goal);
		String comment = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[2]/td")).getText();
		assertEquals("今週も楽しく学べました。", comment);
		String text = webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/table/tbody/tr[3]/td")).getText();
		assertEquals("この一週間で、かなり成長できた気がします。", text);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case8_7_reportDetail.png"));
	}

}
