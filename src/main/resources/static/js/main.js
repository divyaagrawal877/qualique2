// API Base URL
const API_BASE = '/api/public';

// Load company info and populate the page
async function loadCompanyInfo() {
    try {
        const response = await fetch(`${API_BASE}/company`);
        const result = await response.json();
        
        if (result.success && result.data) {
            const company = result.data;
            
            // Update header
            if (company.email) {
                const headerEmail = document.getElementById('header-email');
                if (headerEmail) headerEmail.textContent = company.email;
            }
            if (company.phone) {
                const headerPhone = document.getElementById('header-phone');
                if (headerPhone) headerPhone.textContent = company.phone;
                
                // Update WhatsApp link
                const whatsappFloat = document.getElementById('whatsapp-float');
                if (whatsappFloat) {
                    const phoneNum = company.phone.replace(/[^0-9]/g, '');
                    whatsappFloat.href = `https://wa.me/${phoneNum}`;
                }
            }
            
            // Update social links
            updateSocialLink('social-facebook', 'footer-facebook', company.facebookUrl);
            updateSocialLink('social-instagram', 'footer-instagram', company.instagramUrl);
            updateSocialLink('social-linkedin', 'footer-linkedin', company.linkedinUrl);
            updateSocialLink('social-twitter', 'footer-twitter', company.twitterUrl);
            
            // Update hero section
            const heroTitle = document.getElementById('hero-title');
            if (heroTitle && company.companyName) {
                heroTitle.textContent = `Welcome to ${company.companyName}`;
            }
            const heroTagline = document.getElementById('hero-tagline');
            if (heroTagline && company.tagline) {
                heroTagline.textContent = company.tagline;
            }
            
            // Update about section
            const aboutCompanyName = document.getElementById('about-company-name');
            if (aboutCompanyName && company.companyName) {
                aboutCompanyName.textContent = company.companyName;
            }
            const aboutText = document.getElementById('about-text');
            if (aboutText && company.aboutUs) {
                aboutText.textContent = company.aboutUs.substring(0, 300) + '...';
            }
            
            // Update footer
            const footerCompanyName = document.getElementById('footer-company-name');
            if (footerCompanyName && company.companyName) {
                footerCompanyName.textContent = company.companyName;
            }
            const footerAbout = document.getElementById('footer-about');
            if (footerAbout && company.aboutUs) {
                footerAbout.textContent = company.aboutUs.substring(0, 150) + '...';
            }
            const footerAddress = document.getElementById('footer-address');
            if (footerAddress) {
                footerAddress.textContent = `${company.address || ''}, ${company.city || ''}, ${company.state || ''}`;
            }
            const footerPhone = document.getElementById('footer-phone');
            if (footerPhone && company.phone) {
                footerPhone.textContent = company.phone;
            }
            const footerEmail = document.getElementById('footer-email');
            if (footerEmail && company.email) {
                footerEmail.textContent = company.email;
            }
            const footerCopyright = document.getElementById('footer-copyright');
            if (footerCopyright && company.copyrightText) {
                footerCopyright.textContent = company.copyrightText;
            }
            
            // Update page title
            if (company.companyName) {
                document.title = `${company.companyName} - Industrial Products Supplier`;
            }
        }
    } catch (error) {
        console.error('Error loading company info:', error);
    }
}

function updateSocialLink(headerId, footerId, url) {
    const headerLink = document.getElementById(headerId);
    const footerLink = document.getElementById(footerId);
    
    if (url) {
        if (headerLink) headerLink.href = url;
        if (footerLink) footerLink.href = url;
    } else {
        if (headerLink) headerLink.style.display = 'none';
        if (footerLink) footerLink.style.display = 'none';
    }
}

// Load categories
async function loadCategories() {
    try {
        const response = await fetch(`${API_BASE}/categories`);
        const result = await response.json();
        
        if (result.success && result.data) {
            const categories = result.data;
            
            // Update navigation dropdown
            const navCategories = document.getElementById('nav-categories');
            if (navCategories) {
                navCategories.innerHTML = '<li><a class="dropdown-item" href="/products.html">All Products</a></li>';
                categories.forEach(cat => {
                    navCategories.innerHTML += `<li><a class="dropdown-item" href="/products.html?category=${cat.id}">${cat.name}</a></li>`;
                });
            }
            
            // Update categories container on home page
            const categoriesContainer = document.getElementById('categories-container');
            if (categoriesContainer) {
                if (categories.length === 0) {
                    categoriesContainer.innerHTML = '<div class="col-12 text-center"><p>No categories available yet.</p></div>';
                } else {
                    categoriesContainer.innerHTML = categories.slice(0, 8).map(cat => `
                        <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                            <a href="/products.html?category=${cat.id}" class="text-decoration-none">
                                <div class="category-card">
                                    ${cat.imageUrl 
                                        ? `<img src="${cat.imageUrl}" alt="${cat.name}" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                           <div class="icon" style="display:none;"><i class="${cat.iconClass || 'fas fa-box'}"></i></div>`
                                        : `<div class="icon"><i class="${cat.iconClass || 'fas fa-box'}"></i></div>`
                                    }
                                    <h5>${cat.name}</h5>
                                    <p>${cat.description || 'Explore our products'}</p>
                                </div>
                            </a>
                        </div>
                    `).join('');
                }
            }
            
            // Update footer categories
            const footerCategories = document.getElementById('footer-categories');
            if (footerCategories) {
                footerCategories.innerHTML = categories.slice(0, 5).map(cat => 
                    `<li><a href="/products.html?category=${cat.id}">${cat.name}</a></li>`
                ).join('');
            }
        }
    } catch (error) {
        console.error('Error loading categories:', error);
        const categoriesContainer = document.getElementById('categories-container');
        if (categoriesContainer) {
            categoriesContainer.innerHTML = '<div class="col-12 text-center text-danger"><p>Error loading categories</p></div>';
        }
    }
}

// Load featured products
async function loadFeaturedProducts() {
    const container = document.getElementById('featured-products-container');
    if (!container) return;
    
    try {
        const response = await fetch(`${API_BASE}/products/featured`);
        const result = await response.json();
        
        if (result.success && result.data) {
            const products = result.data;
            
            if (products.length === 0) {
                container.innerHTML = '<div class="col-12 text-center"><p>No featured products yet.</p></div>';
            } else {
                container.innerHTML = products.slice(0, 8).map(product => `
                    <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                        <div class="product-card">
                            <div class="product-image">
                                <img src="${product.imageUrl || 'https://via.placeholder.com/300x200?text=Product'}" alt="${product.name}" 
                                     onerror="this.src='https://via.placeholder.com/300x200?text=Product'">
                            </div>
                            <div class="product-info">
                                <span class="category">${product.categoryName || 'General'}</span>
                                <h5>${product.name}</h5>
                                <p>${(product.description || '').substring(0, 80)}...</p>
                                <a href="/products.html?id=${product.id}" class="btn btn-sm btn-outline-primary mt-2">View Details</a>
                            </div>
                        </div>
                    </div>
                `).join('');
            }
        }
    } catch (error) {
        console.error('Error loading featured products:', error);
        container.innerHTML = '<div class="col-12 text-center text-danger"><p>Error loading products</p></div>';
    }
}

// Load brands
async function loadBrands() {
    const container = document.getElementById('brands-container');
    if (!container) return;
    
    try {
        const response = await fetch(`${API_BASE}/brands`);
        const result = await response.json();
        
        if (result.success && result.data) {
            const brands = result.data;
            
            if (brands.length === 0) {
                container.innerHTML = '<p class="text-center text-muted">No brands available yet.</p>';
            } else {
                container.innerHTML = brands.map(brand => `
                    <div class="brand-item">
                        <img src="${brand.logoUrl || 'https://via.placeholder.com/150x60?text=' + brand.name}" 
                             alt="${brand.name}" 
                             onerror="this.src='https://via.placeholder.com/150x60?text=${brand.name}'">
                    </div>
                `).join('');
            }
        }
    } catch (error) {
        console.error('Error loading brands:', error);
    }
}

// Initialize page
document.addEventListener('DOMContentLoaded', function() {
    loadCompanyInfo();
    loadCategories();
    loadFeaturedProducts();
    loadBrands();
});
