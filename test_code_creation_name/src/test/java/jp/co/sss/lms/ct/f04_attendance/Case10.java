package jp.co.sss.lms.ct.f04_attendance;

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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
						"evidence/case10_01_loginPage.png"));
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
						"evidence/case10_02_success.png"));
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
						"evidence/case10_03_attendance.png"));
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() throws Exception {
		webDriver.findElement(By.name("punchIn")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		scrollBy("100");
		String punchIn = webDriver.findElements(By.className("w80")).get(4)
				.getText();
		assertFalse(punchIn.equals(""));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case10_04_punchIn.png"));
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() throws Exception {
		scrollTo("0");
		webDriver.findElement(By.name("punchOut")).click();

		Alert confirm = webDriver.switchTo().alert();
		confirm.accept();

		scrollBy("100");
		String punchOut = webDriver.findElements(By.className("w80")).get(5)
				.getText();
		assertFalse(punchOut.equals(""));

		File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file,
				new File(
						"evidence/case10_05_punchOut.png"));
	}

}
