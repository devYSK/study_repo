package com.ys.test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

class ArchitectureTest {

	private static final String ROOT_PACKAGE = "com.ys.test";

	JavaClasses javaClasses = new ClassFileImporter()
		.withImportOption(new ImportOption.DoNotIncludeTests())
		.importPackages(ROOT_PACKAGE); // 우리 루트 패키지명

	@BeforeEach
	void beforeEach() {
		javaClasses = new ClassFileImporter()
			.withImportOption(new ImportOption.DoNotIncludeTests())
			.importPackages(ROOT_PACKAGE); // 모든 소스 파일이 대상
	}

	@Test
	@DisplayName("컨트롤러 패키지 안에 있는 클래스들은 Api로 끝나야 한다")
	void controllerTest() {
		ArchRule rule = classes().that()
								 .resideInAnyPackage("..controller")
								 .should()
								 .haveSimpleNameEndingWith("Api");

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("repository 패키지 안에 있는 클래스들은 interface여야 한다")
	void archRepositoryTest() {

		ArchRule rule = classes()
			.that()
			.resideInAnyPackage("..repository..")
			.should()
			.beInterfaces();

		rule.check(javaClasses);
	}

	@Test
	@DisplayName("controller 패키지 안에 있는 클래스들은 RestController나 Controller 어노테이션이여야 한다.")
	void archControllerTest() {
		ArchRule rule = classes()
			.that()
			.resideInAnyPackage("..controller")
			.should()
			.beAnnotatedWith(RestController.class)
			.orShould()
			.beAnnotatedWith(Controller.class);

		rule.check(javaClasses);
	}

	@Test
	void controllerDependencyTest() {
		ArchRule rule = classes()
			.that()
			.resideInAnyPackage("..controller")
			.should()
			.dependOnClassesThat()
			.resideInAnyPackage("..request..", "..response..", "..service..");

		rule.check(javaClasses);
	}

	@DisplayName("Controller는 의존되지 않음")
	@Test
	void controllerDependencyTest2() {

		ArchRule rule = classes()
			.that()
			.resideInAnyPackage("..controller")
			.should()
			.onlyHaveDependentClassesThat()
			.resideInAnyPackage("..controller");

		rule.check(javaClasses);
	}

	@DisplayName("Controller는 Model 사용 불가")
	@Test
	void controllerDependencyTest3() {

		ArchRule rule = noClasses()
			.that()
			.resideInAnyPackage("..controller")
			.should()
			.onlyHaveDependentClassesThat()
			.resideInAnyPackage("..model..");

		rule.check(javaClasses);

	}

	@DisplayName("model은 아무것도 의존하지 않음")
	@Test
	void modelDependencyTest() {
		ArchRule rule = classes()
			.that()
			.resideInAnyPackage("..model..")
			.should()
			.onlyDependOnClassesThat()
			.resideInAnyPackage("..model..", "java..", "jakarta..");

		rule.check(javaClasses);

	}

}
