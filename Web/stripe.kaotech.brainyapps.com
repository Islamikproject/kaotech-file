server {
        listen 80;

        root /usr/share/nginx/html;
        index index.html index.htm;
        server_name stripe.kaotech.brainyapps.com;

        location / {
                # First attempt to serve request as file, then
                # as directory, then fall back to displaying a 404.
                try_files $uri $uri/ =404;
                # Uncomment to enable naxsi on this location
                # include /etc/nginx/naxsi.rules
        }
}

server {
        listen 443 ssl;
        server_name stripe.kaotech.brainyapps.com;
        
#        ssl_certificate /etc/letsencrypt/live/stripe.kaotech.brainyapps.com/fullchain.pem;
#        ssl_certificate_key /etc/letsencrypt/live/stripe.kaotech.brainyapps.com/privkey.pem;
        
        ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
        ssl_prefer_server_ciphers on;
        ssl_dhparam /etc/ssl/certs/dhparam.pem;
        ssl_ciphers 'ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:DHE-DSS-AES128-GCM-SHA256:kEDH+AESGCM:ECDHE-RSA-AES128-SHA256:ECDHE-ECDSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:ECDHE-ECDSA-AES128-SHA:ECDHE-RSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:ECDHE-ECDSA-AES256-SHA:DHE-RSA-AES128-SHA256:DHE-RSA-AES128-SHA:DHE-DSS-AES128-SHA256:DHE-RSA-AES256-SHA256:DHE-DSS-AES256-SHA:DHE-RSA-AES256-SHA:AES128-GCM-SHA256:AES256-GCM-SHA384:AES128-SHA256:AES256-SHA256:AES128-SHA:AES256-SHA:AES:CAMELLIA:DES-CBC3-SHA:!aNULL:!eNULL:!EXPORT:!DES:!RC4:!MD5:!PSK:!aECDH:!EDH-DSS-DES-CBC3-SHA:!EDH-RSA-DES-CBC3-SHA:!KRB5-DES-CBC3-SHA';        ssl_session_timeout 1d;        ssl_session_cache shared:SSL:50m;
        ssl_stapling on;
        ssl_stapling_verify on;
        add_header Strict-Transport-Security max-age=15768000;
        
        root /mnt/HOSTING/stripe.kaotech.brainyapps.com;
        index index.php;
        
        try_files $uri $uri/ @rewrite;
        location @rewrite {
                rewrite ^/(.*)$ /index.php?_url=/$1;
        }       
        location ~ \.php {
#                fastcgi_pass 127.0.0.1:9000;
                fastcgi_pass unix:/run/php/php7.0-fpm.sock;
                fastcgi_index /index.php;
                include /etc/nginx/fastcgi_params;
                fastcgi_split_path_info ^(.+\.php)(/.+)$;
                fastcgi_param PATH_INFO $fastcgi_path_info;
                fastcgi_param PATH_TRANSLATED $document_root$fastcgi_path_info;
                fastcgi_param SCRIPT_FILENAME $document_root$fastcgi_script_name;
                fastcgi_param HTTPS on;
        }       
}       

