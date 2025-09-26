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
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
						"evidence/case17/01_loginPage.png"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() throws Exception {
		WebElement loginId = webDriver.findElement(By.name("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA03");
		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("StudentAA03");

		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[3]/div/input")).click();
		pageLoadTimeout(20);
		String pageTitle = webDriver.getTitle();
		assertEquals("セキュリティ規約 | LMS", pageTitle);
		String welcomeMsg = webDriver.findElement(By.xpath("//*[@id=\"wrap\"]/nav/div/ul/li[1]/a/small")).getText();
		assertEquals("ようこそ受講生ＡＡ３さん", welcomeMsg);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case17/02_success.png"));
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
						"evidence/case17/03_agree.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.name("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA03");

		WebElement password = webDriver.findElement(By.name("password"));
		password.clear();
		password.sendKeys("tisUserAA03");

		WebElement passwordConfirm = webDriver.findElement(By.name("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("tisUserAA03");
		scrollBy("200");

		webDriver.findElement(By.xpath("//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]")).click();
		for (int i = 0; i <= 30; i++) {
			scrollBy("0");
		}
		webDriver.findElement(By.xpath("//*[@id=\"upd-btn\"]")).click();
		pageLoadTimeout(20);
		scrollBy("50");

		String pageTitle = webDriver.getTitle();
		assertEquals("コース詳細 | LMS", pageTitle);

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case17/04_passChangeSuccess.png"));
	}

}
