package com.qualique.controller;

import com.qualique.entity.Category;
import com.qualique.entity.Product;
import com.qualique.repository.CategoryRepository;
import com.qualique.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SitemapController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            baseUrl += ":" + request.getServerPort();
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"\n");
        xml.append("        xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\">\n");

        String today = LocalDate.now().toString();

        // Static pages
        addUrl(xml, baseUrl + "/", today, "daily", "1.0", null, null);
        addUrl(xml, baseUrl + "/about.html", today, "monthly", "0.8", null, null);
        addUrl(xml, baseUrl + "/products.html", today, "daily", "0.9", null, null);
        addUrl(xml, baseUrl + "/gallery.html", today, "weekly", "0.6", null, null);
        addUrl(xml, baseUrl + "/contact.html", today, "monthly", "0.7", null, null);

        // Category pages
        List<Category> categories = categoryRepository.findByActiveTrueOrderByDisplayOrderAsc();
        for (Category cat : categories) {
            String imageUrl = cat.getImageUrl() != null ? resolveUrl(baseUrl, cat.getImageUrl()) : null;
            addUrl(xml, baseUrl + "/products.html?category=" + cat.getId(), today, "weekly", "0.7", imageUrl, cat.getName());
        }

        // Product pages with images
        List<Product> products = productRepository.findByActiveTrueOrderByDisplayOrderAsc();
        for (Product product : products) {
            String imageUrl = product.getImageUrl() != null ? resolveUrl(baseUrl, product.getImageUrl()) : null;
            addUrl(xml, baseUrl + "/products.html?id=" + product.getId(), today, "weekly", "0.6", imageUrl, product.getName());
        }

        xml.append("</urlset>");

        return ResponseEntity.ok(xml.toString());
    }

    private void addUrl(StringBuilder xml, String loc, String lastmod, String changefreq, String priority, String imageUrl, String imageTitle) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(escapeXml(loc)).append("</loc>\n");
        xml.append("    <lastmod>").append(lastmod).append("</lastmod>\n");
        xml.append("    <changefreq>").append(changefreq).append("</changefreq>\n");
        xml.append("    <priority>").append(priority).append("</priority>\n");
        if (imageUrl != null) {
            xml.append("    <image:image>\n");
            xml.append("      <image:loc>").append(escapeXml(imageUrl)).append("</image:loc>\n");
            if (imageTitle != null) {
                xml.append("      <image:title>").append(escapeXml(imageTitle)).append("</image:title>\n");
            }
            xml.append("    </image:image>\n");
        }
        xml.append("  </url>\n");
    }

    private String resolveUrl(String baseUrl, String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        return baseUrl + url;
    }

    private String escapeXml(String input) {
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
