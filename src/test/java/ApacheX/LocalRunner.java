package ApacheX;

/**
 * Use this class for local development.
 * It will run local kafka in docker with test containers.
 */
public class LocalRunner {
  public static void main(String[] args) {
    ApacheX.createApplicationBuilder()
      .initializers(new AbstractIntegrationTest.Initializer())
      .run(args);
  }
}
