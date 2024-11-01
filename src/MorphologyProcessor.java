import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Color;

public class MorphologyProcessor {
    private static int[][] kernel = {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}};

    public static BufferedImage applyOperation(BufferedImage image, String operationType) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        switch (operationType) {
            case "erosion":
                result = erosion(image);
                break;
            case "dilation":
                result = dilation(image);
                break;
            case "opening":
                result = dilation(erosion(image));
                break;
            case "closing":
                result = erosion(dilation(image));
                break;
            case "low_pass":
                result = applyLowPassFilter(image);
                break;
        }
        return result;
    }

    private static BufferedImage erosion(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int minValue = 255;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixelValue = new Color(image.getRGB(x + i, y + j)).getRed();
                        minValue = Math.min(minValue, pixelValue);
                    }
                }

                result.setRGB(x, y, new Color(minValue, minValue, minValue).getRGB());
            }
        }
        return result;
    }

    private static BufferedImage dilation(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                int maxValue = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixelValue = new Color(image.getRGB(x + i, y + j)).getRed();
                        maxValue = Math.max(maxValue, pixelValue);
                    }
                }

                result.setRGB(x, y, new Color(maxValue, maxValue, maxValue).getRGB());
            }
        }
        return result;
    }

    private static BufferedImage applyLowPassFilter(BufferedImage image) {
        float[] kernel = {
                1f / 16, 2f / 16, 1f / 16,
                2f / 16, 4f / 16, 2f / 16,
                1f / 16, 2f / 16, 1f / 16
        };
        ConvolveOp convolveOp = new ConvolveOp(new Kernel(3, 3, kernel), ConvolveOp.EDGE_NO_OP, null);
        return convolveOp.filter(image, null);
    }
}
