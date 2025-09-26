package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

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
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
						"evidence/case16/01_loginPage.png"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() throws Exception {
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA02");
		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA02");

		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[3]/div/input")).click();
		pageLoadTimeout(20);
		String pageTitle = webDriver.getTitle();
		assertEquals("セキュリティ規約 | LMS", pageTitle);
		String welcomeMsg = webDriver.findElement(By.xpath("//*[@id=\"wrap\"]/nav/div/ul/li[1]/a/small")).getText();
		assertEquals("ようこそ受講生ＡＡ２さん", welcomeMsg);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case16/02_success.png"));
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() throws Exception {
		scrollBy("200");
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/form/fieldset/div[1]/div/label")).click();
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/form/fieldset/div[2]/button")).click();
		pageLoadTimeout(20);

		String pageTitle = webDriver.getTitle();
		assertEquals("パスワード変更 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case16/03_agree.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() throws Exception {
		scrollBy("50");
		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("tisUserAA02");

		WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("tisUserAA02");
		scrollBy("200");

		webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]")).click();
		for (int i = 0; i <= 30; i++) {
			scrollBy("0");
		}
		webDriver.findElement(By.xpath("//*[@id=\"upd-btn\"]")).click();
		pageLoadTimeout(20);
		scrollBy("50");

		String errorMsg = webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[1]/div/ul/li/span"))
				.getText();
		assertEquals("現在のパスワードは必須です。", errorMsg);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case16/04_currentPassError.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA02");

		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("tisUser123456789101112");

		WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("tisUser123456789101112");
		scrollBy("200");

		webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]")).click();
		for (int i = 0; i <= 30; i++) {
			scrollBy("0");
		}
		webDriver.findElement(By.xpath("//*[@id=\"upd-btn\"]")).click();
		pageLoadTimeout(20);
		scrollBy("50");

		String errorMsg = webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[2]/div/ul/li/span"))
				.getText();
		assertEquals("パスワードの長さが最大値(20)を超えています。", errorMsg);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case16/05_passOverRangeError.png"));
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA02");

		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("123456789");

		WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("123456789");
		scrollBy("200");

		webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]")).click();
		for (int i = 0; i <= 30; i++) {
			scrollBy("0");
		}
		webDriver.findElement(By.xpath("//*[@id=\"upd-btn\"]")).click();
		pageLoadTimeout(20);
		scrollBy("50");

		String errorMsg = webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[2]/div/ul/li/span"))
				.getText();
		assertEquals("「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。", errorMsg);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case16/06_passIsInvalidError.png"));
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA02");

		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("tisUserAA02");

		WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("tisUserAA01");
		scrollBy("200");

		webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]")).click();
		for (int i = 0; i <= 30; i++) {
			scrollBy("0");
		}
		webDriver.findElement(By.xpath("//*[@id=\"upd-btn\"]")).click();
		pageLoadTimeout(20);
		scrollBy("50");

		String errorMsg = webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[2]/div/ul/li/span"))
				.getText();
		assertEquals("パスワードと確認パスワードが一致しません。", errorMsg);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case16/07_passNotMatchError.png"));
	}

}
