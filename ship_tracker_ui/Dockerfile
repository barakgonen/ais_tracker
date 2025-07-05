# --------- Build stage ---------
FROM node:20 AS build

WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install

COPY . ./
RUN npm run build

# --------- Production stage ---------
FROM nginx:stable-alpine

# Copy built files to nginx html folder
COPY --from=build /app/build /usr/share/nginx/html

# Optional: custom Nginx config
# COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
