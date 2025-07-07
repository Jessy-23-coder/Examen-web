document.addEventListener('DOMContentLoaded', function() {
    // Configuration
    const config = {
        defaultPage: 'accueil',
        contentSelector: '#content',
        navLinksSelector: 'nav a',
        activeClass: 'active',
        animationDuration: 300,
        pagesDir: 'pages'
    };

    // Éléments du DOM
    const contentElement = document.querySelector(config.contentSelector);
    const navLinks = document.querySelectorAll(config.navLinksSelector);

    // Initialisation
    init();

    function init() {
        // Gestion des clics sur les liens de navigation (y compris dropdown)
        document.querySelectorAll(`${config.navLinksSelector}, .dropdown-content a`).forEach(link => {
            link.addEventListener('click', function(e) {
                if (this.hasAttribute('data-page')) {
                    e.preventDefault();
                    const page = this.getAttribute('data-page');
                    navigateTo(page);
                }
            });
        });

        // Gestion des changements d'historique
        window.addEventListener('popstate', handlePopState);

        // Chargement de la page initiale
        const initialPage = getPageFromURL() || config.defaultPage;
        navigateTo(initialPage, false);
    }

    function navigateTo(page, pushState = true) {
        // Animation de transition
        contentElement.classList.add('fade-out');

        setTimeout(() => {
            loadPageContent(page)
                .then(html => {
                    // Mettre à jour le contenu
                    contentElement.innerHTML = html;
                    
                    // Mettre à jour la classe active
                    updateActiveLink(page);
                    
                    // Mettre à jour le titre
                    document.title = `Mon Application SPA - ${getPageTitle(page)}`;
                    
                    // Animation de retour
                    contentElement.classList.remove('fade-out');
                    
                    // Mettre à jour l'historique
                    if (pushState) {
                        history.pushState({ page }, '', `?page=${page}`);
                    }
                })
                .catch(error => {
                    console.error('Erreur:', error);
                    contentElement.innerHTML = `
                        <div class="error">
                            <h1>Erreur 404</h1>
                            <p>La page demandée n'a pas été trouvée.</p>
                            <button onclick="navigateTo('${config.defaultPage}')">Retour à l'accueil</button>
                        </div>
                    `;
                    contentElement.classList.remove('fade-out');
                });
        }, config.animationDuration);
    }

    function loadPageContent(page) {
        return fetch(`${config.pagesDir}/${page}.html`)
            .then(response => {
                if (!response.ok) throw new Error('Page non trouvée');
                return response.text();
            });
    }

    function updateActiveLink(page) {
        // Retirer active de tous les liens
        navLinks.forEach(link => link.classList.remove(config.activeClass));
        
        // Trouver et activer le lien correspondant
        const activeLink = document.querySelector(`[data-page="${page}"]`);
        if (activeLink) {
            activeLink.classList.add(config.activeClass);
            
            // Si c'est un élément de dropdown, activer aussi le parent dropdown
            const dropdownParent = activeLink.closest('.dropdown');
            if (dropdownParent) {
                dropdownParent.querySelector('.dropbtn').classList.add(config.activeClass);
            }
        }
    }

    function handlePopState(e) {
        const page = e.state?.page || config.defaultPage;
        navigateTo(page, false);
    }

    function getPageFromURL() {
        const params = new URLSearchParams(window.location.search);
        return params.get('page');
    }

    function getPageTitle(page) {
        const titles = {
            'accueil': 'Accueil',
            'pret': 'Prêt',
            'ajout_pret': 'Ajouter un prêt',
            'ajout_type_pres': 'Ajouter un type de prêt'
        };
        return titles[page] || capitalizeFirstLetter(page);
    }

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

    // Exposer la fonction navigateTo globalement
    window.navigateTo = navigateTo;
});


// public/js/index.js
document.addEventListener('DOMContentLoaded', function() {
  // Gestion de la navigation
  document.querySelectorAll('[data-page]').forEach(link => {
    link.addEventListener('click', function(e) {
      e.preventDefault();
      // Votre logique de changement de page ici
    });
  });
});