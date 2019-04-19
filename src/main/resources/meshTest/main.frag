#version 450 core

out vec4 FragColor;

struct Material {
    vec3 specular;
    float shininess;
};

struct Light {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

layout(binding = 0) uniform sampler2D texture_image;

layout(location = 0) in vec3 fs_position;
layout(location = 1) in vec3 fs_normal;
layout(location = 2) in vec2 fs_texture_coordinate;

layout(location = 0) out vec4 final_color;

uniform vec3 viewPos;
uniform Material material;
uniform Light light;

void main()
{
    //FragColor = texture(texture_image, fs_texture_coordinate);

    // ambient
    vec3 ambient = light.ambient * texture(texture_image, fs_texture_coordinate).rgb;

    // diffues
    vec3 norm = normalize(fs_normal);
    vec3 lightDir = normalize(light.position - fs_position);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * texture(texture_image, fs_texture_coordinate).rgb;

    // specular
    vec3 viewDir = normalize(viewPos - fs_position);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * (spec * material.specular);

    vec3 result = ambient + diffuse + specular;
    FragColor = vec4(result, 1.0);
}