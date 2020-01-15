package dk.romeri.demo;

import dk.romeri.demo.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SkatBowlingOpgaveApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private HomeController homeController;

	@Test
	void fetchFromAPIAndVerifyScore() throws Exception {
		this.mockMvc.perform(get("/")) // Visit the index page
				.andExpect(status().isOk()); // Make sure we get a 200 request

		assertEquals(homeController.getSuccess(), true);
	}
}
