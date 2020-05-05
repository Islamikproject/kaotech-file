server {
        listen 80;
        server_name stripe.kaotech.org;
        
        add_header Strict-Transport-Security max-age=15768000;
        
        root /home/HOSTING/stripe.kaotech.org;
        index index.php;
        
        try_files $uri $uri/ @rewrite;
        location @rewrite {
                rewrite ^/(.*)$ /index.php?_url=/$1;
        }       
        location ~ \.php {
#               fastcgi_pass 127.0.0.1:9000;
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

