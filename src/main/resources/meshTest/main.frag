#version 450 core

out vec4 FragColor;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

struct DirLight {
    float[3] direction;
    float[3] ambient;
    float[3] diffuse;
    float[3] specular;
};

struct PointLight {
    float[3] position;

    float constant;
    float linear;
    float quadratic;

    float[3] ambient;
    float[3] diffuse;
    float[3] specular;
};

struct SpotLight {
    float[3] position;

    float constant;
    float linear;
    float quadratic;

    float[3] ambient;
    float[3] diffuse;
    float[3] specular;

    float[3] direction;
    float cutOff;
    float outerCutOff;
};

// Lights
uniform int DirLightsSize;
layout(binding = 1, std430) buffer DirLights {
    DirLight dirLights[];
};

uniform int PointLightsSize;
layout(binding = 2, std430) buffer PointLights {
    PointLight pointLights[];
};

uniform int SpotLightsSize;
layout(binding = 3, std430) buffer SpotLights {
    SpotLight spotLights[];
};

// Texture
layout(binding = 0) uniform sampler2D texture_image;

// Model
layout(location = 0) in vec3 fs_position;
layout(location = 1) in vec3 fs_normal;
layout(location = 2) in vec2 fs_texture_coordinate;

// View
uniform vec3 viewPos;

// Material
uniform Material material;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

// Final color
layout(location = 0) out vec4 final_color;

// Lightning functions
vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir);
vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir);
vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir);

void main()
{
    //FragColor = texture(texture_image, fs_texture_coordinate);

    // properties
    vec3 norm = normalize(fs_normal);
    vec3 viewDir = normalize(viewPos - fs_position);

    vec3 result = vec3(0.0, 0.0, 0.0);

   for (int i = 0; i < DirLightsSize; i++) {
        result += CalcDirLight(dirLights[i], norm, viewDir);
    }
    for (int i = 0; i < PointLightsSize; i++) {
        result += CalcPointLight(pointLights[i], norm, fs_position, viewDir);
    }
    for (int i = 0; i < SpotLightsSize; i++) {
        result += CalcSpotLight(spotLights[i], norm, fs_position, viewDir);
    }

    FragColor = vec4(result, 1.0);
}

vec3 ToVec(float[3] arr) {
    return vec3(arr[0], arr[1], arr[2]);
}

// calculates the color when using a directional light.
vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir)
{
    vec3 lightDir = normalize(-ToVec(light.direction));
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    // combine results
    vec3 ambient = ToVec(light.ambient) * vec3(texture(texture_image, fs_texture_coordinate));
    vec3 diffuse =  ToVec(light.diffuse) * diff * vec3(texture(texture_image, fs_texture_coordinate));
    vec3 specular = ToVec(light.specular) * spec * material.specular;
    return (ambient + diffuse + specular);
}

// calculates the color when using a point light.
vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 position = (projection * view * model * vec4(ToVec(pointLights[0].position), 1.0)).xyz;
    vec3 lightDir = normalize(ToVec(light.position) - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    // attenuation
    float distance = length(ToVec(light.position) - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // combine results
    vec3 ambient = ToVec(light.ambient) * vec3(texture(texture_image, fs_texture_coordinate));
    vec3 diffuse = ToVec(light.diffuse) * diff * vec3(texture(texture_image, fs_texture_coordinate));
    vec3 specular = ToVec(light.specular) * spec * material.specular;
    ambient *= attenuation;
    diffuse *= attenuation;
    specular *= attenuation;
    return (ambient + diffuse + specular);
}

// calculates the color when using a spot light.
vec3 CalcSpotLight(SpotLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
    vec3 lightDir = normalize(ToVec(light.position) - fragPos);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    // attenuation
    float distance = length(ToVec(light.position) - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
    // spotlight intensity
    float theta = dot(lightDir, normalize(-ToVec(light.direction)));
    float epsilon = light.cutOff - light.outerCutOff;
    float intensity = clamp((theta - light.outerCutOff) / epsilon, 0.0, 1.0);
    // combine results
    vec3 ambient = ToVec(light.ambient) * vec3(texture(texture_image, fs_texture_coordinate));
    vec3 diffuse = ToVec(light.diffuse) * diff * vec3(texture(texture_image, fs_texture_coordinate));
    vec3 specular = ToVec(light.specular) * spec * material.specular;
    ambient *= attenuation * intensity;
    diffuse *= attenuation * intensity;
    specular *= attenuation * intensity;
    return (ambient + diffuse + specular);
}