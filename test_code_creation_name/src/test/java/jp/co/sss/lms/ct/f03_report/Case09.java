package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
						"evidence/case09/01_loginPage.png"));
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
						"evidence/case09/02_success.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() throws Exception {
		webDriver.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[2]/a")).click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("ユーザー詳細", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/03_userDetail.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws Exception {
		scrollBy("700");
		final List<WebElement> elements = webDriver.findElements(By.tagName("form"));
		WebElement element = elements.get(elements.size() - 8);

		element.click();
		pageLoadTimeout(50);

		String pageTitle = webDriver.getTitle();
		assertEquals("レポート登録 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/04_report.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() throws Exception {
		WebElement learning = webDriver.findElement(By.xpath("//*[@id=\"intFieldName_0\"]"));
		learning.clear();

		scrollBy("700");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/form/div[3]/fieldset/div/div/button")).click();

		WebElement newLearning = webDriver.findElement(By.xpath("//*[@id=\"intFieldName_0\"]"));
		String errorClass = newLearning.getAttribute("class");
		assertEquals("form-control errorInput", errorClass);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/05_learningError.png"));

		newLearning.sendKeys("ITリテラシー①");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() throws Exception {
		Select understanding = new Select(webDriver.findElement(By.tagName("select")));
		understanding.selectByVisibleText("");

		scrollBy("700");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/form/div[3]/fieldset/div/div/button")).click();

		String errorClass = webDriver.findElement(By.tagName("select")).getAttribute("class");
		assertEquals("form-control errorInput", errorClass);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/06_understandingError.png"));

		Select newUnderstanding = new Select(webDriver.findElement(By.tagName("select")));
		newUnderstanding.selectByVisibleText("3");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() throws Exception {
		WebElement goal = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		goal.clear();
		goal.sendKeys("できました。");

		scrollBy("700");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/form/div[3]/fieldset/div/div/button")).click();

		WebElement newGoal = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		String errorClass = newGoal.getAttribute("class");
		assertEquals("form-control errorInput", errorClass);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/07_goalIsNotNumber.png"));

		newGoal.clear();
		newGoal.sendKeys("10");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() throws Exception {
		WebElement goal = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		goal.clear();
		goal.sendKeys("11");

		scrollBy("700");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/form/div[3]/fieldset/div/div/button")).click();

		WebElement newGoal = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		String errorClass = newGoal.getAttribute("class");
		assertEquals("form-control errorInput", errorClass);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/08_goalOverRange.png"));

		newGoal.clear();
		newGoal.sendKeys("10");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() throws Exception {
		scrollBy("200");
		WebElement goal = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		goal.clear();
		WebElement comment = webDriver.findElement(By.xpath("//*[@id=\"content_1\"]"));
		comment.clear();

		scrollBy("700");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/form/div[3]/fieldset/div/div/button")).click();
		scrollBy("200");

		WebElement newGoal = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		String errorGoalClass = newGoal.getAttribute("class");
		WebElement newComment = webDriver.findElement(By.xpath("//*[@id=\"content_1\"]"));
		String errorCommentClass = newComment.getAttribute("class");

		assertEquals("form-control errorInput", errorGoalClass);
		assertEquals("form-control errorInput", errorCommentClass);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/09_commentError.png"));

		newGoal.sendKeys("10");
		newComment.sendKeys("今週も楽しく学べました。");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() throws Exception {
		scrollBy("700");
		WebElement comment = webDriver.findElement(By.xpath("//*[@id=\"content_1\"]"));
		comment.clear();
		comment.sendKeys(StringUtils.repeat("研修", 1001));
		WebElement text = webDriver.findElement(By.xpath("//*[@id=\"content_2\"]"));
		text.clear();
		text.sendKeys(StringUtils.repeat("研修", 1001));
		pageLoadTimeout(50);

		scrollBy("700");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/form/div[3]/fieldset/div/div/button")).click();
		scrollBy("700");

		WebElement newComment = webDriver.findElement(By.xpath("//*[@id=\"content_1\"]"));
		String errorCommentClass = newComment.getAttribute("class");
		WebElement newText = webDriver.findElement(By.xpath("//*[@id=\"content_2\"]"));
		String errorTextClass = newText.getAttribute("class");

		assertEquals("form-control errorInput", errorCommentClass);
		assertEquals("form-control errorInput", errorTextClass);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case09/10_textError.png"));
	}

}
