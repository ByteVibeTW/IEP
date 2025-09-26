## Build the frontend
FROM node:lts AS frontend-build

WORKDIR /app

# Copy the source code and install the dependencies.
COPY web/package.json ./
COPY web/yarn.lock ./
# Install the dependencies and build the application.
RUN --mount=type=cache,target=/root/.yarn YARN_CACHE_FOLDER=/root/.yarn yarn install --frozen-lockfile
COPY web ./
RUN yarn build

## Build the backend
FROM maven:3-amazoncorretto-21-debian AS backend-build
# Copy the source code and build the application.
WORKDIR /app
COPY ./api .
COPY --from=frontend-build /app/dist /app/src/main/resources/static
# Package the application, and extract the layers from the jar for the next stage.
RUN --mount=type=cache,target=/root/.m2 mvn -f pom.xml package -DskipTests && \
    java -Djarmode=tools -jar target/EnergyAudit-0.0.1-SNAPSHOT.jar extract --layers --destination extracted


## Build the runtime image
FROM amazoncorretto:21.0.5-alpine3.20 AS runtime
WORKDIR /app
## 安裝字形，使得中文能正常顯示(Apache POI 套件需要)
RUN apk --no-cache add fontconfig=2.14.2-r1 ttf-dejavu=2.37-r2 tzdata=2024a-r0
## 安裝 Windows 中文字型
COPY Fonts /usr/share/fonts/chinese
RUN fc-cache -f -v

# Copy the extracted layers from the previous stage.
COPY --from=backend-build /app/extracted/dependencies/ ./
COPY --from=backend-build /app/extracted/snapshot-dependencies/ ./
COPY --from=backend-build /app/extracted/spring-boot-loader/ ./
COPY --from=backend-build /app/extracted/application/*.jar ./app.jar

## 啟動應用程式
ENV TZ="Asia/Taipei"
CMD ["java", "-jar", "app.jar"]